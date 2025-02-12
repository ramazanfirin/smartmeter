package com.masterteknoloji.net.service.device.lora;

import org.springframework.stereotype.Service;

import com.masterteknoloji.net.config.ApplicationProperties;
import com.masterteknoloji.net.repository.LorawanMessageRepository;
import com.masterteknoloji.net.repository.SensorRepository;

@Service
public class VibrationSensorService extends BaseLoraDeviceService implements LoraDeviceService{

	public VibrationSensorService(LorawanMessageRepository lorawanMessageRepository,
			SensorRepository sensorRepository, ApplicationProperties applicationProperties) {
		super(lorawanMessageRepository, sensorRepository,applicationProperties);
		// TODO Auto-generated constructor stub
	}

}
