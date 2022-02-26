package com.azure.learn.simulator.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.azure.learn.simulator.model.DeviceSchedulerModel;
import com.azure.learn.simulator.model.IoTMessage;
import com.azure.learn.simulator.model.MessageScheduler;
import com.azure.learn.simulator.support.IoTDeviceHandler.EventCallback;
import com.azure.learn.simulator.vo.ThreadStatus;
import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.Message;

/**
 * @author SANOJ
 *
 */
@Component
public class SchedulerHandler {

	@Autowired
	ConfigContext context;
	@Autowired
	MessageGenerator messageGenerator;
	private Map<String, SchedulerTask> schedulingMap = new ConcurrentHashMap<>();
	private Map<String, DeviceClient> deviceClientMap = new ConcurrentHashMap<>();
	private ExecutorService singleThreadExecuter = Executors.newSingleThreadExecutor();

	/**
	 * @return
	 */
	public List<ThreadStatus> schedulerRunningStatus() {
		List<ThreadStatus> list = new ArrayList<>();
		schedulingMap.forEach((k, v) -> {
			list.add(v.getThreadStatus());
		});
		if (list.isEmpty()) {
			ThreadStatus t = new ThreadStatus();
			t.setThreadName("No Running Thread.....");
			t.setStatus(RunningStatus.NOT_EXISTING);
			t.setExceptionMsg("TASK WAS NOT RUNNING");
			list.add(t);
		}
		return list;
	}

	/**
	 * @return
	 */
	public boolean isdeviceClientExist(String deviceName) {
		return deviceClientMap.get(deviceName) != null;
	}

	/**
	 * @param threadName
	 * @return
	 */
	public SchedulerTask getSchedulerTask(String threadName) {
		return schedulingMap.get(threadName);
	}

	/**
	 * @param threadName
	 */
	public void stopSchedulerTask(String threadName) {
		Optional<SchedulerTask> task = Optional.ofNullable(schedulingMap.get(threadName));
		if (task.isPresent()) {
			String msg = "NO EXCEPTION FOUND : "
					+ (RunningStatus.RUNNING.equals(task.get().getThreadStatus().getStatus()) ? "TASK WAS  RUNNING"
							: "TASK WAS NOT RUNNING");
			if (RunningStatus.RUNNING.equals(task.get().getThreadStatus().getStatus())) {
				msg = msg + " : TASK CANCELLED ";
				task.get().cancel();
			}
			task.get().getThreadStatus().setExceptionMsg(msg);
		}else {
			DeviceSchedulerModel model = context.findMessageScheduler(threadName);
			prepareTimerTask(threadName, model, "NO EXCEPTION FOUND : TASK IS NOT RUNNING");
		}
	}

	/**
	 * @param dConfig
	 * @throws TimeoutException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public void getDeviceClient(DeviceSchedulerModel dConfig)
			throws InterruptedException, ExecutionException, TimeoutException {
		Future<DeviceClient> future = singleThreadExecuter.submit(() -> {
			IoTDeviceHandler handler = new IoTDeviceHandler(dConfig.getSecretKey());
			DeviceClient client = handler.getDeviceClient();
			client.open();
			return client;
		});
		DeviceClient client = future.get(1, TimeUnit.MINUTES);
		deviceClientMap.put(dConfig.getDeviceName(), client);
		System.out.println("Device Client created for Device :" + dConfig.getDeviceName());
	}

	/**
	 * @return
	 */
	public List<String> displayTimerTaskList() {
		List<String> list = new ArrayList<>();
		context.getDeviceConfigs().forEach(d -> d.getSchedulerConfig().forEach(s -> list.add(context.buildThreadName(d, s))));
		return list;
	}

	/**
	 * @param dConfig
	 */
	public void prepareAndExecuteTask(String threadName, DeviceSchedulerModel ms) {
		if (schedulingMap.get(threadName) == null
				|| !RunningStatus.RUNNING.equals(schedulingMap.get(threadName).getThreadStatus().getStatus())) {
			schedulingMap.put(threadName, new SchedulerTask(threadName, ms.getMessageScheduler()));
			scheduleTask(ms.getDeviceName(), threadName);
		}
	}

	/**
	 * @param dConfig
	 * @param exceptionMsg
	 */
	public void prepareTimerTask(String threadName, DeviceSchedulerModel ms, String exceptionMsg) {
		if (schedulingMap.get(threadName) == null
				|| !RunningStatus.RUNNING.equals(schedulingMap.get(threadName).getThreadStatus().getStatus())) {
			schedulingMap.put(threadName, new SchedulerTask(threadName, exceptionMsg));
		}
	}

	/**
	 * 
	 */
	public void prepareAndscheduleTask(String threadName) {
		DeviceSchedulerModel model = context.findMessageScheduler(threadName);
		try {
			if (!isdeviceClientExist(model.getDeviceName())) {
				getDeviceClient(model);
			}
			prepareAndExecuteTask(threadName, model);
		} catch (Exception e) {
			prepareTimerTask(threadName, model, e.getMessage());
		}
	}

	/**
	 * @param deviceName
	 * @param threadName
	 */
	public void scheduleTask(String deviceName, String threadName) {
		SchedulerTask task = schedulingMap.get(threadName);
		if (task != null && RunningStatus.STOPED.equals(task.getThreadStatus().getStatus())
				&& task.getConfig() != null) {
			DeviceClient client = deviceClientMap.get(deviceName);
			TimerTask timerTask = new TimerTask() {
				@Override
				public void run() {
					EventCallback eventCallback = new EventCallback();
					IoTMessage<Double> m = messageGenerator.getMessage(deviceName,
							task.getConfig().getEntityName(), task.getConfig().getParameterName());
					String msgstr = messageGenerator.getString(m);
					Message msg = new Message(msgstr);
					msg.setContentTypeFinal("application/json");
					msg.setMessageId(java.util.UUID.randomUUID().toString());
					msg.setExpiryTime(2000);
					client.sendEventAsync(msg, eventCallback, msg);
					System.out.println("MESSAGE SENT :threadName >>"+threadName+" : MESSAGE >> "+msgstr);
				}
			};
			task.schedule(timerTask);
		} else {
			System.out
					.println("Could not find :" + threadName + " : " + task.getThreadStatus().getExceptionMsg() != null
							? " Exception Occured " + task.getThreadStatus().getExceptionMsg()
							: "");
		}
	}

	class SchedulerTask {
		private MessageScheduler config;
		private Timer timer;
		private ThreadStatus status;
		private String testThreadName;

		public ThreadStatus getThreadStatus() {
			return status;
		}

		public void setThreadStatus(ThreadStatus status) {
			this.status = status;
		}

		public MessageScheduler getConfig() {
			return config;
		}

		SchedulerTask(String threadName, final MessageScheduler c) {
			config = c;
			timer = new Timer(threadName);
			status = new ThreadStatus();
			status.setExceptionMsg("NO EXCEPTION FOUND");
			status.setStatus(RunningStatus.STOPED);
			status.setThreadName(threadName);
			testThreadName = threadName;
		}

		SchedulerTask(String threadName, String exceptionString) {
			status = new ThreadStatus();
			status.setExceptionMsg(exceptionString);
			status.setStatus(RunningStatus.STOPED);
			status.setThreadName(threadName);
			testThreadName = threadName;
		}

		/**
		 * @param task
		 */
		public void schedule(TimerTask task) {
			if (timer != null && config != null && RunningStatus.STOPED.equals(status.getStatus())) {
				timer.scheduleAtFixedRate(task, 0, config.getPeriod());
				status.setStatus(RunningStatus.RUNNING);
			}
		}

		/**
		 * 
		 */
		public void cancel() {
			if (timer != null && RunningStatus.RUNNING.equals(status.getStatus())) {
				System.out.println(" cancelling invoked :" + testThreadName);
				timer.cancel();
				status.setStatus(RunningStatus.STOPED);
			}
		}
	}
}
