package com.azure.learn.simulator.support;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.azure.learn.simulator.model.IoTMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author SANOJ
 *
 */
@Component
public class MessageGenerator {

	@Value("${msg.parameter.temperature}")
	private String paramTemaperatre;
	
	@Value("${msg.parameter.humidity}")
	private String paramHumidity;

	/**
	 * 
	 */
	private static ObjectMapper objMapper = new ObjectMapper();

	/**
	 * @param entityName
	 * @return
	 */
	public IoTMessage<Double> getTemporature(String deviceId, String entityName) {
		IoTMessage<Double> message = new IoTMessage<>();
		message.setDeviceId(deviceId);
		message.setParameterName(paramTemaperatre);
		message.setEntityName(entityName);
		message.setValue(20 + Math.random() * 10);
		return message;
	}

	/**
	 * @param entityName
	 * @return
	 */
	public IoTMessage<Double> getHumidity(String deviceId, String entityName) {
		IoTMessage<Double> message = new IoTMessage<>();
		message.setDeviceId(deviceId);
		message.setEntityName(entityName);
		message.setParameterName(paramHumidity);
		message.setValue(30 + Math.random() * 20);
		return message;
	}

	/**
	 * @param entityName
	 * @param msgkey
	 * @return
	 */
	public IoTMessage<Double> getMessage(String deviceId, String entityName, String msgkey) {
		switch (msgkey) {
		case "temperature":
			return getTemporature(deviceId, entityName);
		case "humidity":
			return getHumidity(deviceId, entityName);
		default:
			System.out.println("Error in parameter name : PARAMETER NAME NOT IDENTIFIED >> "+msgkey);
			return new IoTMessage<Double>();
		}
	}

	/**
	 * @param obj
	 * @return
	 */
	public String getString(IoTMessage<Double> obj) {
		try {
			return objMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
		}
		return "";
	}
}
