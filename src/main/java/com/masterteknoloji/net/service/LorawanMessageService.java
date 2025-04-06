package com.masterteknoloji.net.service;

import java.time.ZonedDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterteknoloji.net.domain.LorawanMessage;
import com.masterteknoloji.net.domain.Sensor;
import com.masterteknoloji.net.domain.enumeration.Type;
import com.masterteknoloji.net.repository.LorawanMessageRepository;
import com.masterteknoloji.net.repository.SensorRepository;
import com.masterteknoloji.net.service.device.lora.CurrentMeterService;
import com.masterteknoloji.net.service.device.lora.VibrationEcoSensorService;
import com.masterteknoloji.net.service.device.lora.VibrationProSensorService;
import com.masterteknoloji.net.service.device.lora.VibrationSensorService;
import com.masterteknoloji.net.service.device.lora.WaterMeterService;
import com.masterteknoloji.net.web.rest.util.LoraMessageUtil;
import com.masterteknoloji.net.web.rest.vm.DeviceMessageVM;

@Service
public class LorawanMessageService {
	
	private ObjectMapper objectMapper= new ObjectMapper();
	
    private final SensorRepository sensorRepository;
    
    private final WaterMeterService waterMeterService;
    
    private final VibrationSensorService vibrationSensorService;
    
    private final CurrentMeterService currentMeterService;
    
    private final LorawanMessageRepository lorawanMessageRepository;
    
    private final VibrationEcoSensorService vibrationEcoSensorService;
    
    private final VibrationProSensorService vibrationProSensorService;
	
	public LorawanMessageService(SensorRepository sensorRepository,WaterMeterService waterMeterService,
			LorawanMessageRepository lorawanMessageRepository,
			VibrationSensorService vibrationSensorService,
			CurrentMeterService currentMeterService,
			VibrationEcoSensorService vibrationEcoSensorService,
			VibrationProSensorService vibrationProSensorService) {
		super();
		this.sensorRepository = sensorRepository;
		this.waterMeterService = waterMeterService;
		this.lorawanMessageRepository = lorawanMessageRepository;
		this.vibrationSensorService = vibrationSensorService;
		this.currentMeterService = currentMeterService;
		this.vibrationEcoSensorService = vibrationEcoSensorService;
		this.vibrationProSensorService = vibrationProSensorService;

	}
	
	public void process(String message) throws Exception {
		DeviceMessageVM deviceMessageVM = convertToDeviceMessage(message);
		
		if(deviceMessageVM.getSensor()==null) {
    		System.out.println("sensor bulunamadı");
    		return;
    		
    	}
		
		if(deviceMessageVM.getData()==null) {
    		System.out.println("Data bulunamadı");
    		return;
    		
    	}
		
		if(deviceMessageVM.getSensor().getType() == Type.WATER_METER)
			waterMeterService.process(deviceMessageVM);
		else if(deviceMessageVM.getSensor().getType() == Type.VIBRATION) 	
			vibrationSensorService.process(deviceMessageVM);
		else if(deviceMessageVM.getSensor().getType() == Type.BUTTON) 	
			currentMeterService.process(deviceMessageVM);
		else if(deviceMessageVM.getSensor().getType() == Type.VIBRATION_ECO) 	
			vibrationEcoSensorService.process(deviceMessageVM);
		else if(deviceMessageVM.getSensor().getType() == Type.VIBRATION_PRO) 	
			vibrationProSensorService.process(deviceMessageVM);
	}

	public DeviceMessageVM convertToDeviceMessage(String message) throws Exception {
		
		DeviceMessageVM loraMessageVM = new DeviceMessageVM();
		JsonNode jsonObject = objectMapper.readTree(message);
		
		loraMessageVM.setJsonNode(jsonObject);
		
		JsonNode deviceInfo = jsonObject.get("deviceInfo");
    	JsonNode devEui = deviceInfo.get("devEui");
    	String eui = devEui.asText();
    	Sensor sensor = sensorRepository.findOneByDevEui(eui.toUpperCase());
		
		loraMessageVM.setSensor(sensor);
		
		JsonNode dataNode = jsonObject.get("data");
		if(dataNode != null) {
			String data=jsonObject.get("data").asText();
			loraMessageVM.setBase64Message(data);
			loraMessageVM.setHexMessage(LoraMessageUtil.base64ToHex(data));
			loraMessageVM.setData(data);
		}
		
		JsonNode objectNode = jsonObject.get("object");
		if(objectNode != null) {
			loraMessageVM.setObjectNode(objectNode);
		}
		
		if(jsonObject.get("fPort") != null)
			loraMessageVM.setfPort(jsonObject.get("fPort").asText());
		if(jsonObject.get("fCnt") != null)
			loraMessageVM.setfCnt(jsonObject.get("fCnt").asLong());
		
		return loraMessageVM;
	}
	

	
}
