package com.azure.learn.simulator.model;

/**
 * @author SANOJ
 *
 */
public class MessageScheduler {
	/**
	 * 
	 */
	private long period;
	/**
	 * 
	 */
	private String parameterName;
	/**
	 * 
	 */
	private String entityName;
	
	/**
	 * @return
	 */
	public long getPeriod() {
		return period;
	}

	/**
	 * @param period
	 */
	public void setPeriod(long period) {
		this.period = period;
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
	public String getEntityName() {
		return entityName;
	}

	/**
	 * @param entityName
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
}
