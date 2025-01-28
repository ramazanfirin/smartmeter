package com.masterteknoloji.net.service.device;

import java.io.IOException;
import java.time.ZonedDateTime;

import org.springframework.stereotype.Service;

import com.masterteknoloji.net.config.ApplicationProperties;
import com.masterteknoloji.net.domain.LorawanMessage;
import com.masterteknoloji.net.domain.Sensor;
import com.masterteknoloji.net.repository.LorawanMessageRepository;
import com.masterteknoloji.net.repository.SensorRepository;
import com.masterteknoloji.net.web.rest.vm.DeviceMessageVM;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class BaseDeviceService implements LoraDeviceService{
	
	 private final LorawanMessageRepository lorawanMessageRepository;	
	
	 private final SensorRepository sensorRepository;
	 
	 private final ApplicationProperties applicationProperties;
	
	public BaseDeviceService(LorawanMessageRepository lorawanMessageRepository, SensorRepository sensorRepository,ApplicationProperties applicationProperties) {
		super();
		this.lorawanMessageRepository = lorawanMessageRepository;
		this.sensorRepository = sensorRepository;
		this.applicationProperties = applicationProperties;
	}


	@Override
	public void process(DeviceMessageVM deviceMessageVM) throws Exception {
		LorawanMessage lorawanMessage = prepareLorawanMessage(deviceMessageVM);
		parseSensorSpecificData(lorawanMessage, deviceMessageVM);
		postProcess(lorawanMessage);
		sendData(deviceMessageVM,lorawanMessage);
	}
	
	
	public LorawanMessage prepareLorawanMessage(DeviceMessageVM deviceMessageVM) throws Exception {
		LorawanMessage lorawanMessage = new LorawanMessage();
		lorawanMessage.setBase64Message(deviceMessageVM.getBase64Message());
       	lorawanMessage.setHexMessage(deviceMessageVM.getHexMessage());
        
        lorawanMessage.setInsertDate(ZonedDateTime.now());
        lorawanMessage.sensor(deviceMessageVM.getSensor());
        lorawanMessage.setfPort(deviceMessageVM.getfPort());
        lorawanMessage.setfCnt(deviceMessageVM.getfCnt());
        
        parseSensorSpecificData(lorawanMessage,deviceMessageVM);
        
        lorawanMessageRepository.save(lorawanMessage);
		
		Sensor sensor = lorawanMessage.getSensor();
        sensor.setLastMessage(lorawanMessage.getHexMessage());
        sensor.setLastSeenDate(ZonedDateTime.now());
        
        sensorRepository.save(sensor);
        
        return lorawanMessage;
	}

	@Override
	public void parseSensorSpecificData(LorawanMessage lorawanMessage, DeviceMessageVM deviceMessageVM) throws Exception {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void postProcess(LorawanMessage lorawanMessage) {
		// TODO Auto-generated method stub
		
	}

	
	public void sendData(String jsonString,String url) throws IOException {

		OkHttpClient client = new OkHttpClient();
 
        RequestBody body = RequestBody.create(jsonString, MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)  // URL'yi burada belirtin
                .post(body)  // POST metodunu kullanıyoruz
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println("Response: " + response.body().string());
            } else {
                System.out.println("Request failed: " + response.code());
            }
        }
		
	}


	@Override
	public void sendData(DeviceMessageVM deviceMessageVM,LorawanMessage lorawanMessage) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
