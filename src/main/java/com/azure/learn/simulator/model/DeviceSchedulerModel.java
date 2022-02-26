package com.azure.learn.simulator.model;

/**
 * @author SANOJ
 *
 */
public class DeviceSchedulerModel {
	/**
	 * 
	 */
	private String deviceName;

	/**
	 * 
	 */
	private String secretKey;

	/**
	 * 
	 */
	private MessageScheduler messageScheduler;

	/**
	 * @return
	 */
	public String getDeviceName() {
		return deviceName;
	}

	/**
	 * @param deviceName
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	/**
	 * @return
	 */
	public String getSecretKey() {
		return secretKey;
	}

	/**
	 * @param secretKey
	 */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	/**
	 * @return
	 */
	public MessageScheduler getMessageScheduler() {
		return messageScheduler;
	}

	/**
	 * @param messageScheduler
	 */
	public void setMessageScheduler(MessageScheduler messageScheduler) {
		this.messageScheduler = messageScheduler;
	}
}
