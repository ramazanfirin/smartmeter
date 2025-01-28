package com.masterteknoloji.net.web.rest.vm;

import com.fasterxml.jackson.databind.JsonNode;
import com.masterteknoloji.net.domain.Sensor;

public class DeviceMessageVM {
	
	Sensor sensor;
	String data;
	String fPort;
	Long fCnt;
	String base64Message;
	String hexMessage;
	JsonNode jsonNode;
	
	
	public Sensor getSensor() {
		return sensor;
	}
	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getfPort() {
		return fPort;
	}
	public void setfPort(String fPort) {
		this.fPort = fPort;
	}
	public String getBase64Message() {
		return base64Message;
	}
	public void setBase64Message(String base64Message) {
		this.base64Message = base64Message;
	}
	public String getHexMessage() {
		return hexMessage;
	}
	public void setHexMessage(String hexMessage) {
		this.hexMessage = hexMessage;
	}
	public JsonNode getJsonNode() {
		return jsonNode;
	}
	public void setJsonNode(JsonNode jsonNode) {
		this.jsonNode = jsonNode;
	}
	public Long getfCnt() {
		return fCnt;
	}
	public void setfCnt(Long fCnt ) {
		this.fCnt = fCnt;
	}

	
}
