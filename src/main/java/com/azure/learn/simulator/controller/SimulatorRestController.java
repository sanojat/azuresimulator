package com.azure.learn.simulator.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azure.learn.simulator.support.ConfigContext;
import com.azure.learn.simulator.support.ConfigHandler;
import com.azure.learn.simulator.support.RunningStatus;
import com.azure.learn.simulator.support.SchedulerHandler;
import com.azure.learn.simulator.support.ThreadCommand;
import com.azure.learn.simulator.vo.ThreadStatus;

/**
 * @author SANOJ
 *
 */
@RestController
@RequestMapping("/simulator")
public class SimulatorRestController {

	/**
	 * 
	 */
	@Autowired
	private ConfigContext context;
	/**
	 * 
	 */
	@Autowired
	private SchedulerHandler handler;

	/**
	 * 
	 */
	@Autowired
	private ConfigHandler configHandler;

	/**
	 * @return
	 */
	@GetMapping(value = "/config")
	public ConfigContext getConfig() {
		return context;
	}

	/**
	 * @return
	 */
	@GetMapping(value = "/thread/list")
	public List<String> displayTaskList() {
		return handler.displayTimerTaskList();
	}

	/**
	 * @return
	 */
	@GetMapping(value = "/thread/status")
	public List<ThreadStatus> threadRunningStatus() {
		return handler.schedulerRunningStatus();
	}

	/**
	 * @return
	 */
	@PostMapping(value = "/thread/action")
	public List<ThreadStatus> prepareScheduling(@RequestBody List<ThreadCommand> status) {
		List<ThreadStatus> statusList = new ArrayList<ThreadStatus>();
		status.forEach(c -> {
			if (!configHandler.isValidThread(c.getThreadName())) {
				ThreadStatus tstatus = new ThreadStatus();
				tstatus.setThreadName(c.getThreadName());
				tstatus.setStatus(RunningStatus.NOT_EXISTING);
				tstatus.setExceptionMsg("INVALID ThreadName : " + c.getThreadName());
				statusList.add(tstatus);
			} else {
				switch (c.getAction()) {
				case RUN:
					handler.prepareAndscheduleTask(c.getThreadName());
					break;
				case STOP:
					handler.stopSchedulerTask(c.getThreadName());
					break;
				case REMOVE:
					
					break;
				default:
					break;
				}
			}
		});
		statusList.addAll(handler.schedulerRunningStatus());
		return statusList;
	}

}
