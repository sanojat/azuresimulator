package com.azure.learn.simulator.model;

import java.util.List;

import com.azure.learn.simulator.support.IoTConfigType;

/**
 * @author SANOJ
 *
 */
public class IoTConfig {
	
	/**
	 * 
	 */
	private String deviceName;
	
	/**
	 * 
	 */
	private IoTConfigType type;
	/**
	 * 
	 */
	private String secretKey;
	/**
	 * 
	 */
	private List<MessageScheduler> schedulerConfig;

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
	public List<MessageScheduler> getSchedulerConfig() {
		return schedulerConfig;
	}

	/**
	 * @param schedulerConfig
	 */
	public void setSchedulerConfig(List<MessageScheduler> schedulerConfig) {
		this.schedulerConfig = schedulerConfig;
	}

	/**
	 * @return the type
	 */
	public IoTConfigType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(IoTConfigType type) {
		this.type = type;
	}
}
