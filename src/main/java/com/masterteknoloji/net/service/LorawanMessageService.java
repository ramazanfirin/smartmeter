package com.masterteknoloji.net.service;

import java.time.ZonedDateTime;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterteknoloji.net.domain.LorawanMessage;
import com.masterteknoloji.net.domain.Sensor;
import com.masterteknoloji.net.domain.enumeration.Type;
import com.masterteknoloji.net.repository.LorawanMessageRepository;
import com.masterteknoloji.net.repository.SensorRepository;
import com.masterteknoloji.net.web.rest.util.LoraMessageUtil;
import com.masterteknoloji.net.web.rest.vm.DeviceMessageVM;

@Service
public class LorawanMessageService {
	
	private ObjectMapper objectMapper= new ObjectMapper();
	
    private final SensorRepository sensorRepository;
    
    private final WaterMeterService waterMeterService;
    
    private final LorawanMessageRepository lorawanMessageRepository;
	
	public LorawanMessageService(SensorRepository sensorRepository,WaterMeterService waterMeterService,LorawanMessageRepository lorawanMessageRepository) {
		super();
		this.sensorRepository = sensorRepository;
		this.waterMeterService = waterMeterService;
		this.lorawanMessageRepository = lorawanMessageRepository;

	}

	public DeviceMessageVM getLoraMessage(String message) throws Exception {
		
		DeviceMessageVM loraMessageVM = new DeviceMessageVM();
		JsonNode jsonObject = objectMapper.readTree(message);
		
		loraMessageVM.setJsonNode(jsonObject);
		loraMessageVM.setSensor(findSensor(jsonObject));
		
		JsonNode dataNode = jsonObject.get("data");
		if(dataNode != null) {
			String data=jsonObject.get("data").asText();
			loraMessageVM.setBase64Message(data);
			loraMessageVM.setHexMessage(LoraMessageUtil.base64ToHex(data));
			loraMessageVM.setData(data);
		}
		if(jsonObject.get("fPort") != null)
			loraMessageVM.setfPort(jsonObject.get("fPort").asText());
		if(jsonObject.get("fCnt") != null)
			loraMessageVM.setfCnt(jsonObject.get("fCnt").asLong());
		
		return loraMessageVM;
	}
	
	public Sensor findSensor(JsonNode jsonObject) {
    	String devEui = getDeviceEui(jsonObject);
    	Sensor sensor = sensorRepository.findOneByDevEui(devEui.toUpperCase());
		return sensor;
    	
    }
	
	public String getDeviceEui(JsonNode jsonObject) {
    	
    	JsonNode deviceInfo = jsonObject.get("deviceInfo");
    	JsonNode devEui = deviceInfo.get("devEui");
    	
    	return devEui.asText();
    }
	
	public LorawanMessage prepareLorawanMessage(DeviceMessageVM deviceMessageVM) throws Exception {
		LorawanMessage lorawanMessage = new LorawanMessage();
		lorawanMessage.setBase64Message(deviceMessageVM.getBase64Message());
       	lorawanMessage.setHexMessage(deviceMessageVM.getHexMessage());
        
        lorawanMessage.setInsertDate(ZonedDateTime.now());
        lorawanMessage.sensor(deviceMessageVM.getSensor());
        lorawanMessage.setfPort(deviceMessageVM.getfPort());
        lorawanMessage.setfCnt(deviceMessageVM.getfCnt());
        
        parseSensorSpecificData(lorawanMessage, deviceMessageVM);
        
        return lorawanMessage;
	}
    
	public void parseSensorSpecificData(LorawanMessage lorawanMessage,DeviceMessageVM deviceMessageVM) throws Exception {
		if(deviceMessageVM.getSensor().getType().equals(Type.WATER_METER)) {
    		waterMeterService.parseSensorSpecificData(lorawanMessage, deviceMessageVM);
    	}
	}
	
	public void save(LorawanMessage lorawanMessage) {
		lorawanMessageRepository.save(lorawanMessage);
		
		Sensor sensor = lorawanMessage.getSensor();
        sensor.setLastMessage(lorawanMessage.getHexMessage());
        sensor.setLastSeenDate(ZonedDateTime.now());
        
        sensorRepository.save(sensor);
	}
	
	public void postProcess(LorawanMessage lorawanMessage) {
		if(lorawanMessage.getSensor().getType().equals(Type.WATER_METER)) {
    		waterMeterService.postProcess(lorawanMessage);
    	}
	}

}
