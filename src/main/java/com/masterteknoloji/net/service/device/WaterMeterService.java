package com.masterteknoloji.net.service.device;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.masterteknoloji.net.domain.LorawanMessage;
import com.masterteknoloji.net.repository.LorawanMessageRepository;
import com.masterteknoloji.net.repository.SensorRepository;
import com.masterteknoloji.net.web.rest.util.LoraMessageUtil;
import com.masterteknoloji.net.web.rest.vm.DeviceMessageVM;

@Service
public class WaterMeterService extends BaseDeviceService implements LoraDeviceService{
	
	private final LorawanMessageRepository lorawanMessageRepository;
	
	public WaterMeterService(LorawanMessageRepository lorawanMessageRepository, SensorRepository sensorRepository) {
		super(lorawanMessageRepository, sensorRepository);
		this.lorawanMessageRepository = lorawanMessageRepository;
	}

	

	public void parseSensorSpecificData(LorawanMessage lorawanMessage, DeviceMessageVM deviceMessageVM) throws Exception {
		JsonNode object = deviceMessageVM.getJsonNode().get("object");
		if (object.get("BatV") != null)
			lorawanMessage.setBatteryValue((float) object.get("BatV").asDouble());
		if (object.get("Reading") != null)
			lorawanMessage.setSensorValue((float) object.get("Reading").asDouble());

		parseImageData(lorawanMessage, deviceMessageVM);
	}

	public void parseImageData(LorawanMessage lorawanMessage, DeviceMessageVM deviceMessageVM) throws Exception {

		if (deviceMessageVM.getfPort().equals("3")) {
			JsonNode object = deviceMessageVM.getJsonNode().get("object");
			Long subcontractingCount = object.get("subcontracting_count").asLong();
			Long totalPackages = object.get("total_packages").asLong();

			lorawanMessage.setTotalMessageCount(totalPackages);
			lorawanMessage.setIndex(subcontractingCount);

			String data = deviceMessageVM.getJsonNode().get("data").asText();
			String hexMessage = LoraMessageUtil.base64ToHex(data);

			String imageId = LoraMessageUtil.getFirst14Chars(hexMessage);
			lorawanMessage.setImageId(imageId);

			String imageData = LoraMessageUtil.removeFirst8Chars(hexMessage);
			lorawanMessage.setHexMessage(imageData);
			lorawanMessage.setImage(imageData.getBytes());

		}

	}
	
	 private void combineAllImages(LorawanMessage lorawanMessage) {
		    Long fcntEnd = lorawanMessage.getfCnt();
		    Long fcntStart = fcntEnd-lorawanMessage.getTotalMessageCount();
		    
	    	List<LorawanMessage>  list = lorawanMessageRepository.findByFcnt(lorawanMessage.getSensor().getId(),fcntStart,fcntEnd);
	    	
	    	lorawanMessage.setImage(null);
	    	lorawanMessage.setHexMessage("");
	    	
	    	byte[] result=null;
	    	String resultString="";
	    	for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				LorawanMessage temp = (LorawanMessage) iterator.next();
				result = LoraMessageUtil.appendToByteArray(result, temp.getImage());
				resultString = resultString + temp.getHexMessage();
			}
	    	//lorawanMessage.setHexMessage(resultString);
	    	lorawanMessage.setImage(hexStringToByteArray(resultString));
	    	lorawanMessageRepository.save(lorawanMessage);
	    }
	 
	 public byte[] hexStringToByteArray(String hex) {
		    int len = hex.length();
		    byte[] data = new byte[len / 2];
		    for (int i = 0; i < len; i += 2) {
		        data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
		                              + Character.digit(hex.charAt(i + 1), 16));
		    }
		    return data;
		}

	@Override
	public void process(DeviceMessageVM deviceMessageVM) throws Exception {
		super.process(deviceMessageVM);
		
	}

	@Override
	public void postProcess(LorawanMessage lorawanMessage) {
		super.postProcess(lorawanMessage);
		if(lorawanMessage.getfPort().equals("3")) {
        	if(lorawanMessage.getIndex() == lorawanMessage.getTotalMessageCount())
        		combineAllImages(lorawanMessage);
        }
		
	}

	@Override
	public void sendData(LorawanMessage lorawanMessage) {
		// TODO Auto-generated method stub
		
	}
}
