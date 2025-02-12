package com.masterteknoloji.net.service.device.m2m;

import com.masterteknoloji.net.domain.LorawanMessage;
import com.masterteknoloji.net.domain.M2mMessage;
import com.masterteknoloji.net.web.rest.vm.DeviceMessageVM;

public interface M2mDeviceService {
	
	M2mMessage prepareM2mMessage(DeviceMessageVM deviceMessageVM)  throws Exception;
	void parseSensorSpecificData(M2mMessage prepareM2mMessage,DeviceMessageVM deviceMessageVM) throws Exception ;
	void process(DeviceMessageVM deviceMessageVM) throws Exception ;
	void postProcess(M2mMessage m2mMessage) throws Exception ;
	void sendData(DeviceMessageVM deviceMessageVM,M2mMessage m2mMessage) throws Exception ;

}
