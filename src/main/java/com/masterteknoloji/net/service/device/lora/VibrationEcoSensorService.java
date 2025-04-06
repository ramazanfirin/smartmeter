package com.masterteknoloji.net.service.device.lora;

import java.util.Random;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterteknoloji.net.config.ApplicationProperties;
import com.masterteknoloji.net.domain.LorawanMessage;
import com.masterteknoloji.net.domain.Sensor;
import com.masterteknoloji.net.domain.VibrationEcoMessage;
import com.masterteknoloji.net.repository.LorawanMessageRepository;
import com.masterteknoloji.net.repository.SensorRepository;
import com.masterteknoloji.net.repository.VibrationEcoMessageRepository;
import com.masterteknoloji.net.web.rest.util.LoraMessageUtil;
import com.masterteknoloji.net.web.rest.vm.DeviceMessageVM;
import com.masterteknoloji.net.web.rest.vm.thingsboard.VibrationEcoSensorVM;

@Service
public class VibrationEcoSensorService extends BaseLoraDeviceService implements LoraDeviceService{

    private final LorawanMessageRepository lorawanMessageRepository;
	
	private final ApplicationProperties applicationProperties;
	
	private final SensorRepository sensorRepository;
	
	private final VibrationEcoMessageRepository vibrationEcoMessageRepository;
	
	private final ObjectMapper objectMapper;
	
	public VibrationEcoSensorService(LorawanMessageRepository lorawanMessageRepository,
			SensorRepository sensorRepository, ApplicationProperties applicationProperties,
			VibrationEcoMessageRepository vibrationEcoMessageRepository,ObjectMapper objectMapper) {
		super(lorawanMessageRepository, sensorRepository,applicationProperties);
		
		this.applicationProperties = applicationProperties;
		this.lorawanMessageRepository = lorawanMessageRepository;
		this.sensorRepository = sensorRepository;
		this.vibrationEcoMessageRepository = vibrationEcoMessageRepository;
		this.objectMapper = objectMapper;
		
		

	}

//	@Override
//	public VibrationEcoMessage parseSensorSpecificData(LorawanMessage lorawanMessage, DeviceMessageVM deviceMessageVM) throws Exception {
//		VibrationEcoMessage vibrationEcoMessage = new VibrationEcoMessage();
//		vibrationEcoMessage.setBatteryValue(null);
//		vibrationEcoMessage.setxAxisValue(null);
//		vibrationEcoMessage.setyAxisValue(null);
//		vibrationEcoMessage.setzAxisValue(null);
//		
//		return vibrationEcoMessageRepository.save(vibrationEcoMessage);
//	}
	
	//@Scheduled(fixedDelay = 15000)
	public void test() {
		try {
			LorawanMessage lorawanMessage = new LorawanMessage();
			lorawanMessage.setHexMessage("00 3F 24 01 00 0040 0080 FC40 00");
			Sensor sensor = sensorRepository.findOneByDevEui("test1");
			lorawanMessage.setSensor(sensor);
			VibrationEcoMessage  ecoMessage=  (VibrationEcoMessage)parseSensorSpecificData(lorawanMessage, null);;
			sendData(null, lorawanMessage, ecoMessage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public Object parseSensorSpecificData(LorawanMessage lorawanMessage, DeviceMessageVM deviceMessageVM)
			throws Exception {
		VibrationEcoMessage vibrationEcoMessage = new VibrationEcoMessage();
		VibrationEcoSensorVM vibrationSensorVM = parseHexData(deviceMessageVM);
		vibrationEcoMessage.setBatteryValue(0f);
		vibrationEcoMessage.setxAxisValue(vibrationSensorVM.getxAxisValue());
		vibrationEcoMessage.setyAxisValue(vibrationSensorVM.getyAxisValue());
		vibrationEcoMessage.setzAxisValue(vibrationSensorVM.getzAxisValue());
		vibrationEcoMessage.setLoraMessage(lorawanMessage);
		return vibrationEcoMessageRepository.save(vibrationEcoMessage);
	}
	
	@Override
	public void postProcess(LorawanMessage lorawanMessage) {
		
		
	}

	
	@Override
	public void sendData(DeviceMessageVM deviceMessageVM,LorawanMessage lorawanMessage,Object object) throws Exception {
		
		try {
			VibrationEcoMessage ecoMessage = (VibrationEcoMessage)object;
			
			VibrationEcoSensorVM vibrationSensorVM = new VibrationEcoSensorVM(ecoMessage.getxAxisValue(), ecoMessage.getyAxisValue(), ecoMessage.getzAxisValue(), 0f);
			String json = objectMapper.writeValueAsString(vibrationSensorVM);	

			
			sendData(lorawanMessage.getSensor(), json);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public VibrationEcoSensorVM parseHexData(DeviceMessageVM deviceMessageVM) {
		
		Float xAxisValue = 0f;
		Float yAxisValue = 0f;
		Float zAxisValue = 0f;
		
		Float angle = 0f;
		Float battery = 0f;
		Float res = 0f;
		Float evt = 0f;
		
		JsonNode object = deviceMessageVM.getJsonNode().get("object");
		if (object.get("acceX") != null)
			xAxisValue =((float) object.get("acceX").asDouble());
		if (object.get("acceY") != null)
			yAxisValue =((float) object.get("acceY").asDouble());
		if (object.get("acceZ") != null)
			zAxisValue =((float) object.get("acceZ").asDouble());
		
		if (object.get("angle") != null)
			angle =((float) object.get("angle").asDouble());
		
		if (object.get("battery") != null)
			battery =((float) object.get("battery").asDouble());
		
		if (object.get("res") != null)
			res =((float) object.get("res").asDouble());
		
		if (object.get("evt") != null)
			evt =((float) object.get("evt").asDouble());
		
		VibrationEcoSensorVM vibrationSensorVM = new VibrationEcoSensorVM(xAxisValue, yAxisValue, zAxisValue, 0F);
		return vibrationSensorVM;
	}
}
