package com.masterteknoloji.net.service.device.m2m;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterteknoloji.net.config.ApplicationProperties;
import com.masterteknoloji.net.domain.M2mMessage;
import com.masterteknoloji.net.repository.M2mMessageRepository;
import com.masterteknoloji.net.repository.SensorRepository;
import com.masterteknoloji.net.web.rest.util.LoraMessageUtil;
import com.masterteknoloji.net.web.rest.vm.DeviceMessageVM;

@Service
public class WaterMeterServiceM2M extends BaseM2mDeviceService implements M2mDeviceService{
	
    private final Logger log = LoggerFactory.getLogger(WaterMeterServiceM2M.class);
	
	private final M2mMessageRepository m2mMessageRepository;
	
	private final ApplicationProperties applicationProperties;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public WaterMeterServiceM2M(M2mMessageRepository m2mMessageRepository, SensorRepository sensorRepository,ApplicationProperties applicationProperties) {
		super(m2mMessageRepository, sensorRepository,applicationProperties);
		this.m2mMessageRepository = m2mMessageRepository;
		this.applicationProperties = applicationProperties;
		
		//m2mMessageRepository.deleteAll();
	}

	

	public void parseSensorSpecificData(M2mMessage m2mMessage, DeviceMessageVM deviceMessageVM) throws Exception {
		if (!deviceMessageVM.getIsImageData()) {
			JsonNode object = deviceMessageVM.getJsonNode();
			if (object.get("battery") != null)
				m2mMessage.setBatteryValue((float) object.get("battery").asDouble());
			if (object.get("Reading") != null)
				m2mMessage.setSensorValue((float) object.get("Reading").asDouble());
			m2mMessage.setImage(new byte[0]);
			log.info("Veri  datası geldi");
		}else {
			log.info("Image  datası geldi");
			parseImageData(m2mMessage, deviceMessageVM);
		}
		
		m2mMessageRepository.save(m2mMessage);
	}

	public void parseImageData(M2mMessage m2mMessage, DeviceMessageVM deviceMessageVM) throws Exception {
			String hexMessage = deviceMessageVM.getHexMessage();
			String imageData="";
			if(hexMessage.startsWith("P1")){
				imageData = LoraMessageUtil.removeFirst8Chars(16,hexMessage);
			}else {
				imageData = LoraMessageUtil.removeFirst8Chars(2,hexMessage);
			}
			m2mMessage.setHexMessage(hexMessage);
			m2mMessage.setImage(LoraMessageUtil.hexStringToByteArray(imageData));
	}
	
	 private void combineAllImages(M2mMessage m2mMessage) throws IOException {
		 
            if(!m2mMessage.isImageData())
            	return;
		    
	    	List<M2mMessage>  list = m2mMessageRepository.findBySensorId(m2mMessage.getSensor().getId(),m2mMessage.getPort());
	    	M2mMessage firstData = list.get(0);
	    	
	    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    	outputStream.write(firstData.getImage());
	        outputStream.write(m2mMessage.getImage());
	    	
	        firstData.setImage(outputStream.toByteArray());
	        firstData.setValidImage(isValidImage(outputStream));
	        m2mMessageRepository.save(firstData);
	        log.info("Image eklemesi yapıldı.");
	    }
	 
	 public boolean isValidImage(ByteArrayOutputStream baos) {
	        try {
	            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
	            BufferedImage image = ImageIO.read(bais);
	            return image != null;
	        } catch (IOException e) {
	            return false;
	        }
	    } 

	@Override
	public void process(DeviceMessageVM deviceMessageVM) throws Exception {
		super.process(deviceMessageVM);
		
	}

	@Override
	public void postProcess(M2mMessage m2mMessage) throws IOException {
		super.postProcess(m2mMessage);
		combineAllImages(m2mMessage);
	}

	@Override
	public void sendData(DeviceMessageVM deviceMessageVM,M2mMessage m2mMessage) throws Exception {
		
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
