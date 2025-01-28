package com.masterteknoloji.net.service.device;

import com.masterteknoloji.net.domain.LorawanMessage;
import com.masterteknoloji.net.web.rest.vm.DeviceMessageVM;

public interface LoraDeviceService {
	
	LorawanMessage prepareLorawanMessage(DeviceMessageVM deviceMessageVM)  throws Exception;
	void parseSensorSpecificData(LorawanMessage lorawanMessage,DeviceMessageVM deviceMessageVM) throws Exception ;
	void process(DeviceMessageVM deviceMessageVM) throws Exception ;
	void postProcess(LorawanMessage lorawanMessage) throws Exception ;
	void sendData(LorawanMessage lorawanMessage) throws Exception ;

}
