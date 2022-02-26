package com.azure.learn.simulator;

import java.util.ArrayList;

import com.azure.learn.simulator.model.ConfigModel;
import com.azure.learn.simulator.model.IoTConfig;
import com.azure.learn.simulator.support.IoTConfigType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {
	static ObjectMapper obj = new ObjectMapper();

	public static void main(String[] args) throws JsonProcessingException {
		ConfigModel config=new ConfigModel();
		config.setIotConfigs(new ArrayList<>());
		IoTConfig c=new IoTConfig();
		c.setType(IoTConfigType.IOTDEVICE);
		config.getIotConfigs().add(c);
		System.out.println(obj.writeValueAsString(config));
	}
}
