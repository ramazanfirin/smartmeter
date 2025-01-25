package com.masterteknoloji.net.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.masterteknoloji.net.domain.LorawanMessage;
import com.masterteknoloji.net.repository.LorawanMessageRepository;
import com.masterteknoloji.net.web.rest.util.LoraMessageUtil;
import com.masterteknoloji.net.web.rest.vm.DeviceMessageVM;

@Service
public class WaterMeterService {
	
	private final LorawanMessageRepository lorawanMessageRepository;

	public WaterMeterService(LorawanMessageRepository lorawanMessageRepository) {
		super();
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

			lorawanMessage.setImage(imageData.getBytes());

		}

	}
	
	public void postProcess(LorawanMessage lorawanMessage) {
		if(lorawanMessage.getfPort().equals("3")) {
        	if(lorawanMessage.getIndex()+1 == lorawanMessage.getTotalMessageCount())
        		combineAllImages(lorawanMessage);
        }
	}
	
	 private void combineAllImages(LorawanMessage lorawanMessage) {
		    Long fcnt = lorawanMessage.get
		 
	    	List<LorawanMessage>  list = lorawanMessageRepository.findByFcnt(lorawanMessage.getSensor());
	    	
	    	lorawanMessage.setImage(null);
	    	byte[] result=null;
	    	for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				LorawanMessage temp = (LorawanMessage) iterator.next();
				 result = LoraMessageUtil.appendToByteArray(result, temp.getImage());
			}
	    	lorawanMessage.setImage(result);
	    	lorawanMessageRepository.save(lorawanMessage);
	    }
}
