package com.azure.learn.simulator.support;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.azure.learn.simulator.model.ConfigModel;
import com.azure.learn.simulator.model.DeviceSchedulerModel;
import com.azure.learn.simulator.model.IoTConfig;
import com.azure.learn.simulator.model.MessageScheduler;

/**
 * @author SANOJ
 *
 */
@Component
public class ConfigContext {

	/**
	 * 
	 */
	@Value("${threadname.separator}")
	private String threadNameSeparator;

	/**
	 * 
	 */
	private ConfigModel configModel;

	/**
	 * 
	 */
	private Map<String, DeviceSchedulerModel> deviceSchConfigMap = new ConcurrentHashMap<String, DeviceSchedulerModel>();

	/**
	 * @param configModel
	 */
	public void setConfigModel(ConfigModel configModel) {
		this.configModel = configModel;
	}

	/**
	 * @return
	 */
	public List<IoTConfig> getDeviceConfigs() {
		return configModel.getIotConfigs();
	}

	/**
	 * @return
	 */
	public ConfigModel getConfigModel() {
		return configModel;
	}

	public void initIoTDeviceschedulerConfigMap() {
		if (configModel != null) {
			configModel.getIotConfigs().stream().filter(p -> IoTConfigType.IOTDEVICE.equals(p.getType())).forEach(m -> {
				m.getSchedulerConfig().forEach(d -> {
					DeviceSchedulerModel model = new DeviceSchedulerModel();
					model.setDeviceName(m.getDeviceName());
					model.setSecretKey(m.getSecretKey());
					model.setMessageScheduler(d);
					deviceSchConfigMap.put(buildThreadName(m.getDeviceName(), d), model);
				});
			});
		}
	}

	/**
	 * @param threadName
	 * @return
	 */
	public DeviceSchedulerModel findMessageScheduler(String threadName) {
		return deviceSchConfigMap.get(threadName);
	}

	/**
	 * @param dConfig
	 * @param sch
	 * @return
	 */
	public String buildThreadName(IoTConfig dConfig, MessageScheduler sch) {
		return buildThreadName(dConfig.getDeviceName(), sch);
	}

	/**
	 * @param deviceName
	 * @param sch
	 * @return
	 */
	public String buildThreadName(String deviceName, MessageScheduler sch) {
		return deviceName + threadNameSeparator + sch.getEntityName() + threadNameSeparator + sch.getParameterName();
	}
}
