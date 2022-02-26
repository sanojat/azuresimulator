package com.azure.learn.simulator.support;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.azure.learn.simulator.model.ConfigModel;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author SANOJ
 *
 */
@Component
public class ConfigHandler {

	/**
	 * 
	 */
	@Value("classpath:configuration.json")
	private Resource configFile;

	/**
	 * 
	 */
	private ObjectMapper mapper = new ObjectMapper();
	/**
	 * 
	 */
	@Autowired
	private ConfigContext context;

	/**
	 * @throws IOException
	 */
	@PostConstruct
	public void loadConfigs() throws IOException {
		if (context.getConfigModel() == null) {
			context.setConfigModel(mapper.readValue(configFile.getFile(), ConfigModel.class));
			context.initIoTDeviceschedulerConfigMap();
		}
	}
	
	public boolean isValidThread(String threadName) {
		return context.findMessageScheduler(threadName)!=null;
	}

}
