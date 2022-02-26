package com.azure.learn.simulator.model;

/**
 * @author SANOJ
 *
 * @param <V>
 */
public class IoTMessage<V> {

	/**
	 * 
	 */
	private String deviceId;

	/**
	 * 
	 */
	private String entityName;
	/**
	 * 
	 */
	private String parameterName;
	/**
	 * 
	 */
	private V value;

	/**
	 * @return
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * @param entityName
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	/**
	 * @return
	 */
	public String getParameterName() {
		return parameterName;
	}

	/**
	 * @param parameterName
	 */
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	/**
	 * @return
	 */
	public V getValue() {
		return value;
	}

	/**
	 * @param value
	 */
	public void setValue(V value) {
		this.value = value;
	}

	/**
	 * @return
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
