package com.azure.learn.simulator.support;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.IotHubConnectionStatusChangeCallback;
import com.microsoft.azure.sdk.iot.device.IotHubConnectionStatusChangeReason;
import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;
import com.microsoft.azure.sdk.iot.device.IotHubMessageResult;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.transport.IotHubConnectionStatus;

/**
 * @author SANOJ
 *
 */
public class IoTDeviceHandler {

	/**
	 * 
	 */
	private static long SAS_TOKEN_EXPIRY_TIME = 2400;
	/**
	 * 
	 */
	private static final int D2C_MESSAGE_TIMEOUT = 2000;

	/**
	 * 
	 */
	private String connectionString = null;

	/**
	 * 
	 */
	private static final List<String> failedMessageListOnClose = new ArrayList<>();

	/**
	 * @param connectionString
	 */
	public IoTDeviceHandler(String connectionString) {
		this.connectionString = connectionString;
	}

	/**
	 * @return
	 * @throws IllegalArgumentException
	 * @throws URISyntaxException
	 */
	public DeviceClient getDeviceClient() throws IllegalArgumentException, URISyntaxException {
		DeviceClient client = new DeviceClient(this.connectionString, IotHubClientProtocol.MQTT);
		client.setOption("SetSASTokenExpiryTime", SAS_TOKEN_EXPIRY_TIME);
		client.setMessageCallback((m, c) -> {
			Counter counter = (Counter) c;
			System.out.println("Received message " + counter.toString() + " with content: "
					+ new String(m.getBytes(), Message.DEFAULT_IOTHUB_MESSAGE_CHARSET));
			//Arrays.stream(m.getProperties()).forEach(e -> System.out.println(e.getName() + " : " + e.getValue()));
			counter.increment();
			return IotHubMessageResult.COMPLETE;
		}, new Counter(0));
		client.registerConnectionStatusChangeCallback(new IotHubConnectionStatusChangeCallbackLogger(), new Object());
		return client;
	}

	protected static class Counter {
		protected int num;

		public Counter() {
			this.num = 0;
		}

		public Counter(int num) {
			this.num = num;
		}

		public int get() {
			return this.num;
		}

		public void increment() {
			this.num++;
		}

		@Override
		public String toString() {
			return Integer.toString(this.num);
		}
	}

	protected static class EventCallback implements IotHubEventCallback {
		public void execute(IotHubStatusCode status, Object context) {
			Message msg = (Message) context;
			System.out.println("IoT Hub responded to message " + msg.getMessageId() + " with status " + status.name());
			if (status == IotHubStatusCode.MESSAGE_CANCELLED_ONCLOSE) {
				failedMessageListOnClose.add(msg.getMessageId());
			}
		}
	}

	/**
	 * @param messageString
	 * @return
	 */
	public Message getMessage(String messageString) {
		Message msg = new Message(messageString);
		msg.setContentTypeFinal("application/json");
		msg.setMessageId(java.util.UUID.randomUUID().toString());
		msg.setExpiryTime(D2C_MESSAGE_TIMEOUT);
		return msg;
	}

	protected static class IotHubConnectionStatusChangeCallbackLogger implements IotHubConnectionStatusChangeCallback {
		@Override
		public void execute(IotHubConnectionStatus status, IotHubConnectionStatusChangeReason statusChangeReason,
				Throwable throwable, Object callbackContext) {
			System.out.println("CONNECTION STATUS UPDATE: " + status);
			System.out.println("CONNECTION STATUS REASON: " + statusChangeReason);
			System.out.println("CONNECTION STATUS THROWABLE: " + (throwable == null ? "null" : throwable.getMessage()));
			System.out.println();

			if (throwable != null) {
				throwable.printStackTrace();
			}

			if (status == IotHubConnectionStatus.DISCONNECTED) {
				System.out.println("The connection was lost, and is not being re-established."
						+ " Look at provided exception for how to resolve this issue."
						+ " Cannot send messages until this issue is resolved, and you manually re-open the device client");
			} else if (status == IotHubConnectionStatus.DISCONNECTED_RETRYING) {
				System.out.println("The connection was lost, but is being re-established."
						+ " Can still send messages, but they won't be sent until the connection is re-established");
			} else if (status == IotHubConnectionStatus.CONNECTED) {
				System.out.println("The connection was successfully established. Can send messages.");
			}
		}
	}
}
