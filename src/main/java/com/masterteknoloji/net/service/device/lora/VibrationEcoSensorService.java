package com.masterteknoloji.net.service.device.lora;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterteknoloji.net.config.ApplicationProperties;
import com.masterteknoloji.net.domain.LorawanMessage;
import com.masterteknoloji.net.domain.VibrationEcoMessage;
import com.masterteknoloji.net.repository.LorawanMessageRepository;
import com.masterteknoloji.net.repository.SensorRepository;
import com.masterteknoloji.net.repository.VibrationEcoMessageRepository;
import com.masterteknoloji.net.web.rest.vm.DeviceMessageVM;
import com.masterteknoloji.net.web.rest.vm.thingsboard.VibrationSensorVM;

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
	
	@Override
	public Object parseSensorSpecificData(LorawanMessage lorawanMessage, DeviceMessageVM deviceMessageVM)
			throws Exception {
		VibrationEcoMessage vibrationEcoMessage = new VibrationEcoMessage();
		vibrationEcoMessage.setBatteryValue(null);
		vibrationEcoMessage.setxAxisValue(null);
		vibrationEcoMessage.setyAxisValue(null);
		vibrationEcoMessage.setzAxisValue(null);
		
		return vibrationEcoMessageRepository.save(vibrationEcoMessage);
	}
	
	@Override
	public void postProcess(LorawanMessage lorawanMessage) {
		
		
	}

	
	@Override
	public void sendData(DeviceMessageVM deviceMessageVM,LorawanMessage lorawanMessage,Object object) throws Exception {
		
		try {
			VibrationEcoMessage ecoMessage = (VibrationEcoMessage)object;
			
			VibrationSensorVM vibrationSensorVM = new VibrationSensorVM(ecoMessage.getxAxisValue(), ecoMessage.getyAxisValue(), ecoMessage.getzAxisValue(), 0f);
			String json = objectMapper.writeValueAsString(vibrationSensorVM);	

			
			sendData(lorawanMessage.getSensor(), json);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
