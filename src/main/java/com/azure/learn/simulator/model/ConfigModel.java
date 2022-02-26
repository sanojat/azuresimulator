package com.azure.learn.simulator.model;

import java.util.List;

/**
 * @author SANOJ
 *
 */
public class ConfigModel {
	
	/**
	 * 
	 */
	List<IoTConfig> iotConfigs;

	/**
	 * @return
	 */
	public List<IoTConfig> getIotConfigs() {
		return iotConfigs;
	}

	/**
	 * @param iotConfigs
	 */
	public void setIotConfigs(List<IoTConfig> iotConfigs) {
		this.iotConfigs = iotConfigs;
	}

}
