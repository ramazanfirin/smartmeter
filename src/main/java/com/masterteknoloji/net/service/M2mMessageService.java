package com.masterteknoloji.net.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterteknoloji.net.domain.M2mMessage;
import com.masterteknoloji.net.domain.Sensor;
import com.masterteknoloji.net.domain.enumeration.Type;
import com.masterteknoloji.net.repository.M2mMessageRepository;
import com.masterteknoloji.net.repository.SensorRepository;
import com.masterteknoloji.net.service.device.lora.CurrentMeterService;
import com.masterteknoloji.net.service.device.lora.VibrationSensorService;
import com.masterteknoloji.net.service.device.m2m.WaterMeterServiceM2M;
import com.masterteknoloji.net.web.rest.vm.DeviceMessageVM;

@Service
public class M2mMessageService {

	private ObjectMapper objectMapper = new ObjectMapper();

	private final SensorRepository sensorRepository;

	private final WaterMeterServiceM2M waterMeterService;
	
	private ConcurrentHashMap<Long, Sensor> sensorMap = new ConcurrentHashMap<Long, Sensor>();
	
	private final M2mMessageRepository m2mMessageRepository;

	public M2mMessageService(SensorRepository sensorRepository, WaterMeterServiceM2M waterMeterService,
			VibrationSensorService vibrationSensorService, CurrentMeterService currentMeterService,M2mMessageRepository m2mMessageRepository) {
		super();
		this.sensorRepository = sensorRepository;
		this.waterMeterService = waterMeterService;
		this.m2mMessageRepository = m2mMessageRepository;

	}

	public void process(String message,Long port,String ip) throws Exception {
		DeviceMessageVM deviceMessageVM = convertToDeviceMessageForM2m(message,port,ip);

		if (deviceMessageVM.getSensor() == null) {
			System.out.println("sensor bulunamadı");
			return;

		}

		if (deviceMessageVM.getSensor().getType() == Type.WATER_METER)
			waterMeterService.process(deviceMessageVM);

	}

	
	private DeviceMessageVM convertToDeviceMessageForM2m(String message, Long port,String ip) throws Exception {

		DeviceMessageVM deviceMessageVM = new DeviceMessageVM();
		deviceMessageVM.setPort(port);
		deviceMessageVM.setIp(ip);
		
		if (message.startsWith("P")) {
			deviceMessageVM.setIsImageData(true);
			deviceMessageVM.setHexMessage(message);
			//deviceMessageVM.setSensor(sensorMap.get(port));
		}else {
			JsonNode jsonObject = objectMapper.readTree(message);
			deviceMessageVM.setJsonNode(jsonObject);
		}
		deviceMessageVM.setSensor(getSensor(deviceMessageVM));
		return deviceMessageVM;
	}
	
	public Sensor getSensor(DeviceMessageVM deviceMessageVM) {
		if(!deviceMessageVM.getIsImageData()) {
			JsonNode imei = deviceMessageVM.getJsonNode().get("IMEI");
			String imeiValue = imei.asText();
     		Sensor sensor = sensorRepository.findOneByImei(imeiValue);
     		return sensor;
		}else {
			List<M2mMessage> list = m2mMessageRepository.getLastMessage(deviceMessageVM.getIp(),deviceMessageVM.getPort(),ZonedDateTime.now().minusMinutes(1));
			if(list.size()== 0)
				throw new RuntimeException("last message not found");
			
			return list.get(0).getSensor();
		}
	
	}

}
