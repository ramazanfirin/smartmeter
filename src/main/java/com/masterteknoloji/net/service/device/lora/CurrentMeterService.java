package com.masterteknoloji.net.service.device.lora;

import java.util.Random;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
	
	public CurrentSensorVM parseHexData(LorawanMessage lorawanMessage) {
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
		
		CurrentSensorVM currentSensorVM = new CurrentSensorVM();
		currentSensorVM.setBattery(0f);
		currentSensorVM.setCurrent(0f);
		currentSensorVM.setTotalEnergy(0f);
		currentSensorVM.setReason("");
		currentSensorVM.setCableTemperature(0f);
		return currentSensorVM;
	}
}
