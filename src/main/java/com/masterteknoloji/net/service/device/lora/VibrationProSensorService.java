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
import com.masterteknoloji.net.domain.VibrationProMessage;
import com.masterteknoloji.net.repository.LorawanMessageRepository;
import com.masterteknoloji.net.repository.SensorRepository;
import com.masterteknoloji.net.repository.VibrationEcoMessageRepository;
import com.masterteknoloji.net.repository.VibrationProMessageRepository;
import com.masterteknoloji.net.web.rest.util.LoraMessageUtil;
import com.masterteknoloji.net.web.rest.vm.DeviceMessageVM;
import com.masterteknoloji.net.web.rest.vm.thingsboard.VibrationEcoSensorVM;
import com.masterteknoloji.net.web.rest.vm.thingsboard.VibrationProSensorVM;

import liquibase.pro.packaged.v;

@Service
public class VibrationProSensorService extends BaseLoraDeviceService implements LoraDeviceService{

    private final LorawanMessageRepository lorawanMessageRepository;
	
	private final ApplicationProperties applicationProperties;
	
	private final SensorRepository sensorRepository;
	
	private final VibrationProMessageRepository vibrationProMessageRepository;
	
	private final ObjectMapper objectMapper;
	
	public VibrationProSensorService(LorawanMessageRepository lorawanMessageRepository,
			SensorRepository sensorRepository, ApplicationProperties applicationProperties,
			VibrationProMessageRepository vibrationProMessageRepository,ObjectMapper objectMapper) {
		super(lorawanMessageRepository, sensorRepository,applicationProperties);
		
		this.applicationProperties = applicationProperties;
		this.lorawanMessageRepository = lorawanMessageRepository;
		this.sensorRepository = sensorRepository;
		this.vibrationProMessageRepository = vibrationProMessageRepository;
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
		VibrationProMessage vibrationProMessage = new VibrationProMessage();
		VibrationProSensorVM vibrationSensorVM = parseHexData(deviceMessageVM);
		vibrationProMessage.setBatteryValue(vibrationSensorVM.getVoltage());
		vibrationProMessage.setTemperature(vibrationSensorVM.getTemperature());
		
		vibrationProMessage.setxAcceleration(vibrationSensorVM.getxAcceleration());
		vibrationProMessage.setxVelocity(vibrationSensorVM.getxVelocity());
		
		vibrationProMessage.setyAcceleration(vibrationSensorVM.getyAcceleration());
		vibrationProMessage.setyVelocity(vibrationSensorVM.getyVelocity());
		
		vibrationProMessage.setzAcceleration(vibrationSensorVM.getzAcceleration());
		vibrationProMessage.setzVelocity(vibrationSensorVM.getzVelocity());
		
		vibrationProMessage.setLoraMessage(lorawanMessage);
		
		return vibrationProMessageRepository.save(vibrationProMessage);
	}
	
	@Override
	public void postProcess(LorawanMessage lorawanMessage) {
		
		
	}

	
	@Override
	public void sendData(DeviceMessageVM deviceMessageVM,LorawanMessage lorawanMessage,Object object) throws Exception {
		
		try {
			VibrationProMessage message = (VibrationProMessage)object;
			
			VibrationProSensorVM vibrationSensorVM = new VibrationProSensorVM();
			vibrationSensorVM.setTemperature(message.getTemperature());
			vibrationSensorVM.setVoltage(message.getBatteryValue());
			
			vibrationSensorVM.setxAcceleration(message.getxAcceleration());
			vibrationSensorVM.setxVelocity(message.getxVelocity());

			vibrationSensorVM.setyAcceleration(message.getyAcceleration());
			vibrationSensorVM.setyVelocity(message.getyVelocity());

			vibrationSensorVM.setzAcceleration(message.getzAcceleration());
			vibrationSensorVM.setxVelocity(message.getzVelocity());

			
			String json = objectMapper.writeValueAsString(vibrationSensorVM);	

			
			sendData(lorawanMessage.getSensor(), json);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public VibrationProSensorVM parseHexData(DeviceMessageVM deviceMessageVM) {
		Float xVelocity = 0f;
		Float xAcceleration = 0f;
		Float yVelocity = 0f;
		Float yAcceleration = 0f;
		Float zVelocity = 0f;
		Float zAcceleration = 0f;
		
		Float temperature = 0f;
		Float voltage = 0f;
		
		JsonNode object = deviceMessageVM.getJsonNode().get("object");
		if (object.get("xVelocity") != null)
			xVelocity =((float) object.get("xVelocity").asDouble());
		if (object.get("xAcceleration") != null)
			xAcceleration =((float) object.get("xAcceleration").asDouble());
		
		if (object.get("yVelocity") != null)
			yVelocity =((float) object.get("yVelocity").asDouble());
		if (object.get("yAcceleration") != null)
			yAcceleration =((float) object.get("yAcceleration").asDouble());
		
		if (object.get("zVelocity") != null)
			zVelocity =((float) object.get("zVelocity").asDouble());
		if (object.get("zAcceleration") != null)
			zAcceleration =((float) object.get("zAcceleration").asDouble());
		
		
		if (object.get("temperature") != null)
			temperature =((float) object.get("temperature").asDouble());
		
		if (object.get("voltage") != null)
			voltage =((float) object.get("voltage").asDouble());
		
		
		
		VibrationProSensorVM vibrationProSensorVM = new VibrationProSensorVM();
		vibrationProSensorVM.setTemperature(temperature);
		vibrationProSensorVM.setVoltage(voltage);
		vibrationProSensorVM.setxAcceleration(xAcceleration);
		vibrationProSensorVM.setxVelocity(xVelocity);
		vibrationProSensorVM.setyAcceleration(yAcceleration);
		vibrationProSensorVM.setyVelocity(yVelocity);
		vibrationProSensorVM.setzAcceleration(zAcceleration);
		vibrationProSensorVM.setzVelocity(zVelocity);
		
		return vibrationProSensorVM;
	}
}
