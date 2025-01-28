package com.masterteknoloji.net.service.device;

import org.springframework.stereotype.Service;

import com.masterteknoloji.net.repository.LorawanMessageRepository;
import com.masterteknoloji.net.repository.SensorRepository;

@Service
public class CurrentMeterService extends BaseDeviceService implements LoraDeviceService{

	public CurrentMeterService(LorawanMessageRepository lorawanMessageRepository, SensorRepository sensorRepository) {
		super(lorawanMessageRepository, sensorRepository);
		// TODO Auto-generated constructor stub
	}

}
