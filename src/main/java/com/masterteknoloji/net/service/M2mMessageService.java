package com.masterteknoloji.net.service;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterteknoloji.net.domain.Sensor;
import com.masterteknoloji.net.domain.enumeration.Type;
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

	public M2mMessageService(SensorRepository sensorRepository, WaterMeterServiceM2M waterMeterService,
			VibrationSensorService vibrationSensorService, CurrentMeterService currentMeterService) {
		super();
		this.sensorRepository = sensorRepository;
		this.waterMeterService = waterMeterService;

	}

	public void process(String message,Long port) throws Exception {
		DeviceMessageVM deviceMessageVM = convertToDeviceMessageForM2m(message,port);

		if (deviceMessageVM.getSensor() == null) {
			System.out.println("sensor bulunamadı");
			return;

		}

		if (deviceMessageVM.getSensor().getType() == Type.WATER_METER)
			waterMeterService.process(deviceMessageVM);

	}

	
	private DeviceMessageVM convertToDeviceMessageForM2m(String message, Long port) throws Exception {

		DeviceMessageVM deviceMessageVM = new DeviceMessageVM();
		deviceMessageVM.setPort(port);
		if (message.startsWith("P")) {
			deviceMessageVM.setIsImageData(true);
			deviceMessageVM.setHexMessage(message);
			deviceMessageVM.setSensor(sensorMap.get(port));
		} else {

			JsonNode jsonObject = objectMapper.readTree(message);
			deviceMessageVM.setJsonNode(jsonObject);

			JsonNode imei = jsonObject.get("IMEI");
			String imeiValue = imei.asText();
			Sensor sensor = sensorRepository.findOneByImei(imeiValue);

			deviceMessageVM.setSensor(sensor);
			sensorMap.put(port, sensor);
		}
		return deviceMessageVM;
	}
	
	

}
