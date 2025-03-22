package com.masterteknoloji.net.service.device.lora;

import org.springframework.stereotype.Service;

import com.masterteknoloji.net.config.ApplicationProperties;
import com.masterteknoloji.net.domain.LorawanMessage;
import com.masterteknoloji.net.repository.LorawanMessageRepository;
import com.masterteknoloji.net.repository.SensorRepository;
import com.masterteknoloji.net.web.rest.vm.DeviceMessageVM;

@Service
public class VibrationEcoSensorService extends BaseLoraDeviceService implements LoraDeviceService{

    private final LorawanMessageRepository lorawanMessageRepository;
	
	private final ApplicationProperties applicationProperties;
	
	private final SensorRepository sensorRepository;
	
	public VibrationEcoSensorService(LorawanMessageRepository lorawanMessageRepository,
			SensorRepository sensorRepository, ApplicationProperties applicationProperties) {
		super(lorawanMessageRepository, sensorRepository,applicationProperties);
		
		this.applicationProperties = applicationProperties;
		this.lorawanMessageRepository = lorawanMessageRepository;
		this.sensorRepository = sensorRepository;
	}

	@Override
	public void parseSensorSpecificData(LorawanMessage lorawanMessage, DeviceMessageVM deviceMessageVM) throws Exception {
		
	}
	
	@Override
	public void postProcess(LorawanMessage lorawanMessage) {
		
		
	}

	
	@Override
	public void sendData(DeviceMessageVM deviceMessageVM,LorawanMessage lorawanMessage) throws Exception {
		
//		String deviceToken = "46zdi8vwnloavhrnot77";
//		
//		Double value = deviceMessageVM.getObjectNode().get("Reading").asDouble();
//		SmartMeterVM smartMeterVM = new SmartMeterVM(value.floatValue());
//		String json = objectMapper.writeValueAsString(smartMeterVM);
//		
//        String url = applicationProperties.getThingsBoardUrl()+deviceToken+"/telemetry ";
//		super.sendData(json, url);
	}
}
