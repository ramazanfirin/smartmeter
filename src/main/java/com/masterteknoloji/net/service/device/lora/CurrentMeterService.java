package com.masterteknoloji.net.service.device.lora;

import java.util.Random;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterteknoloji.net.config.ApplicationProperties;
import com.masterteknoloji.net.domain.CurrentMeterMessage;
import com.masterteknoloji.net.domain.LorawanMessage;
import com.masterteknoloji.net.domain.Sensor;
import com.masterteknoloji.net.domain.VibrationEcoMessage;
import com.masterteknoloji.net.repository.CurrentMeterMessageRepository;
import com.masterteknoloji.net.repository.LorawanMessageRepository;
import com.masterteknoloji.net.repository.SensorRepository;
import com.masterteknoloji.net.web.rest.util.LoraMessageUtil;
import com.masterteknoloji.net.web.rest.vm.DeviceMessageVM;
import com.masterteknoloji.net.web.rest.vm.thingsboard.CurrentSensorVM;
import com.masterteknoloji.net.web.rest.vm.thingsboard.VibrationEcoSensorVM;

@Service
public class CurrentMeterService extends BaseLoraDeviceService implements LoraDeviceService{

	
	private final LorawanMessageRepository lorawanMessageRepository;
	private final SensorRepository sensorRepository;
	private final ApplicationProperties applicationProperties;
	private final CurrentMeterMessageRepository currentMeterMessageRepository;
	private final ObjectMapper objectMapper;
	
	public CurrentMeterService(LorawanMessageRepository lorawanMessageRepository, SensorRepository sensorRepository, 
			ApplicationProperties applicationProperties,CurrentMeterMessageRepository currentMeterMessageRepository,ObjectMapper objectMapper) {
		super(lorawanMessageRepository, sensorRepository,applicationProperties);
        
		this.lorawanMessageRepository = lorawanMessageRepository;
		this.sensorRepository = sensorRepository;
		this.applicationProperties = applicationProperties;
		this.currentMeterMessageRepository = currentMeterMessageRepository;
		this.objectMapper = objectMapper;
	}

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
		CurrentMeterMessage currentMeterMessage = new CurrentMeterMessage();
		
		CurrentSensorVM currentSensorVM = parseHexData(lorawanMessage);
		currentMeterMessage.setBatteryValue(currentSensorVM.getBattery());
		currentMeterMessage.setCurrent(currentSensorVM.getCurrent());
		currentMeterMessage.setTotalEnergy(currentSensorVM.getTotalEnergy());
		currentMeterMessage.setReason(currentSensorVM.getReason());
		
		return currentMeterMessageRepository.save(currentMeterMessage);
	}
	
	@Override
	public void postProcess(LorawanMessage lorawanMessage) {
		
		
	}

	
	@Override
	public void sendData(DeviceMessageVM deviceMessageVM,LorawanMessage lorawanMessage,Object object) throws Exception {
		
		try {
			CurrentMeterMessage ecoMessage = (CurrentMeterMessage)object;
			
			CurrentSensorVM currentSensorVM = new CurrentSensorVM();
			currentSensorVM.setBattery(ecoMessage.getBatteryValue());
			currentSensorVM.setCurrent(ecoMessage.getCurrent());
			currentSensorVM.setTotalEnergy(ecoMessage.getCurrent());
			currentSensorVM.setReason(ecoMessage.getReason());
			String json = objectMapper.writeValueAsString(currentSensorVM);	

			
			sendData(lorawanMessage.getSensor(), json);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public CurrentSensorVM parseHexData(DeviceMessageVM deviceMessageVM) {
		Float current = 0f;
		Float total_current = 0f;
		Float battery = 0f;

		
		JsonNode object = deviceMessageVM.getJsonNode().get("object");
		if (object.get("current") != null)
			current =((float) object.get("current").asDouble());
		
		if (object.get("total_current") != null)
			total_current =((float) object.get("total_current").asDouble());
		
		//TO DO incele
//		if (object.get("alarm ") != null)
//			total_current =((float) object.get("alarm ").asDouble());
//		if (object.get("current_min  ") != null)
//			total_current =((float) object.get("current_min  ").asDouble());
//		if (object.get("current_max   ") != null)
//			total_current =((float) object.get("current_max   ").asDouble());
		
		CurrentSensorVM currentSensorVM = new CurrentSensorVM();
		currentSensorVM.setBattery(0f);
		currentSensorVM.setCurrent(current);
		currentSensorVM.setTotalEnergy(total_current);
		currentSensorVM.setReason("");
		currentSensorVM.setCableTemperature(0f);
		return currentSensorVM;
	}
}
