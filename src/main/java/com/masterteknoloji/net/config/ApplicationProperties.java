package com.masterteknoloji.net.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Smartmeter.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
	
	private String thingsBoardUrl;
	
	private String mqttBrokerUrl;
	
	private String mqttUsername;
	
	private String mqttPassword;
	
	private String mqttSubTopicName;
	

	public String getThingsBoardUrl() {
		return thingsBoardUrl;
	}

	public void setThingsBoardUrl(String thingsBoardUrl) {
		this.thingsBoardUrl = thingsBoardUrl;
	}

	public String getMqttBrokerUrl() {
		return mqttBrokerUrl;
	}

	public void setMqttBrokerUrl(String mqttBrokerUrl) {
		this.mqttBrokerUrl = mqttBrokerUrl;
	}

	public String getMqttUsername() {
		return mqttUsername;
	}

	public void setMqttUsername(String mqttUsername) {
		this.mqttUsername = mqttUsername;
	}

	public String getMqttPassword() {
		return mqttPassword;
	}

	public void setMqttPassword(String mqttPassword) {
		this.mqttPassword = mqttPassword;
	}

	public String getMqttSubTopicName() {
		return mqttSubTopicName;
	}

	public void setMqttSubTopicName(String mqttSubTopicName) {
		this.mqttSubTopicName = mqttSubTopicName;
	}

}
