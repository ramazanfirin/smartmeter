package com.masterteknoloji.net.service.device.lora;

import java.util.Random;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
	
	@Scheduled(fixedDelay = 15000)
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
		VibrationSensorVM vibrationSensorVM = parseHexData(lorawanMessage);
		vibrationEcoMessage.setBatteryValue(0f);
		vibrationEcoMessage.setxAxisValue(vibrationSensorVM.getxAxisValue());
		vibrationEcoMessage.setyAxisValue(vibrationSensorVM.getyAxisValue());
		vibrationEcoMessage.setzAxisValue(vibrationSensorVM.getzAxisValue());
		
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
	
	public VibrationSensorVM parseHexData(LorawanMessage lorawanMessage) {
		Float xAxisValue = 0F;
		Float yAxisValue = 0F;
		Float zAxisValue = 0F;
		
		String[] values = LoraMessageUtil.parseHex(lorawanMessage.getHexMessage());
		String xHexValue = values[5]+values[6];
		String yHexValue = values[7]+values[8];
		String zHexValue = values[9]+values[10];
				
		int xIntvalue =Integer.parseInt(xHexValue, 16);
		short xSignedValue = (short) xIntvalue;
		
		int yIntvalue =Integer.parseInt(yHexValue, 16);
		short ySignedValue = (short) yIntvalue;
		
		int zIntvalue =Integer.parseInt(zHexValue, 16);
		short zSignedValue = (short) zIntvalue;
		
		xAxisValue = (float)xSignedValue;
		yAxisValue = (float)ySignedValue;
		zAxisValue = (float)zSignedValue;
		
		Random random = new Random();
		xAxisValue = 1 + random.nextFloat() * 99;
		yAxisValue = 1 + random.nextFloat() * 99;
		zAxisValue = 1 + random.nextFloat() * 99;
		
		System.out.println(xAxisValue+","+yAxisValue+","+zAxisValue);
		
		VibrationSensorVM vibrationSensorVM = new VibrationSensorVM(xAxisValue, yAxisValue, zAxisValue, 0F);
		return vibrationSensorVM;
	}
}
