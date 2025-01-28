package com.masterteknoloji.net.service.device;

import java.time.ZonedDateTime;

import org.springframework.stereotype.Service;

import com.masterteknoloji.net.domain.LorawanMessage;
import com.masterteknoloji.net.domain.Sensor;
import com.masterteknoloji.net.repository.LorawanMessageRepository;
import com.masterteknoloji.net.repository.SensorRepository;
import com.masterteknoloji.net.web.rest.vm.DeviceMessageVM;

@Service
public class BaseDeviceService implements LoraDeviceService{
	
	 private final LorawanMessageRepository lorawanMessageRepository;	
	
	 private final SensorRepository sensorRepository;
	
	public BaseDeviceService(LorawanMessageRepository lorawanMessageRepository, SensorRepository sensorRepository) {
		super();
		this.lorawanMessageRepository = lorawanMessageRepository;
		this.sensorRepository = sensorRepository;
	}


	@Override
	public void process(DeviceMessageVM deviceMessageVM) throws Exception {
		LorawanMessage lorawanMessage = prepareLorawanMessage(deviceMessageVM);
		parseSensorSpecificData(lorawanMessage, deviceMessageVM);
		postProcess(lorawanMessage);
		sendData(lorawanMessage);
	}
	
	
	public LorawanMessage prepareLorawanMessage(DeviceMessageVM deviceMessageVM) throws Exception {
		LorawanMessage lorawanMessage = new LorawanMessage();
		lorawanMessage.setBase64Message(deviceMessageVM.getBase64Message());
       	lorawanMessage.setHexMessage(deviceMessageVM.getHexMessage());
        
        lorawanMessage.setInsertDate(ZonedDateTime.now());
        lorawanMessage.sensor(deviceMessageVM.getSensor());
        lorawanMessage.setfPort(deviceMessageVM.getfPort());
        lorawanMessage.setfCnt(deviceMessageVM.getfCnt());
        
        parseSensorSpecificData(lorawanMessage,deviceMessageVM);
        
        lorawanMessageRepository.save(lorawanMessage);
		
		Sensor sensor = lorawanMessage.getSensor();
        sensor.setLastMessage(lorawanMessage.getHexMessage());
        sensor.setLastSeenDate(ZonedDateTime.now());
        
        sensorRepository.save(sensor);
        
        return lorawanMessage;
	}

	@Override
	public void parseSensorSpecificData(LorawanMessage lorawanMessage, DeviceMessageVM deviceMessageVM) throws Exception {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void postProcess(LorawanMessage lorawanMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendData(LorawanMessage lorawanMessage) {
		// TODO Auto-generated method stub
		
	}
}
