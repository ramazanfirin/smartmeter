package com.masterteknoloji.net.service.device.m2m;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.masterteknoloji.net.config.ApplicationProperties;
import com.masterteknoloji.net.domain.LorawanMessage;
import com.masterteknoloji.net.domain.M2mMessage;
import com.masterteknoloji.net.domain.Sensor;
import com.masterteknoloji.net.repository.LorawanMessageRepository;
import com.masterteknoloji.net.repository.M2mMessageRepository;
import com.masterteknoloji.net.repository.SensorRepository;
import com.masterteknoloji.net.web.rest.vm.DeviceMessageVM;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class BaseM2mDeviceService implements M2mDeviceService{
	
	 private final M2mMessageRepository m2mMessageRepository;	
	
	 private final SensorRepository sensorRepository;
	 
	 private final ApplicationProperties applicationProperties;
	
	public BaseM2mDeviceService(M2mMessageRepository m2mMessageRepository, SensorRepository sensorRepository,ApplicationProperties applicationProperties) {
		super();
		this.m2mMessageRepository = m2mMessageRepository;
		this.sensorRepository = sensorRepository;
		this.applicationProperties = applicationProperties;
	}


	@Override
	public void process(DeviceMessageVM deviceMessageVM) throws Exception {
		M2mMessage m2mMessage = prepareM2mMessage(deviceMessageVM);
		parseSensorSpecificData(m2mMessage, deviceMessageVM);
		postProcess(m2mMessage);
		sendData(deviceMessageVM,m2mMessage);
	}
	
	
	public  M2mMessage prepareM2mMessage(DeviceMessageVM deviceMessageVM) throws Exception {

		
        M2mMessage m2mMessage = new M2mMessage();
        //TODO  doldur
        //m2mMessage.setBase64Message(deviceMessageVM.getBase64Message());
        m2mMessage.setHexMessage(deviceMessageVM.getHexMessage());
        m2mMessage.setInsertDate(ZonedDateTime.now());
        m2mMessage.sensor(deviceMessageVM.getSensor());
        m2mMessage.setPort(deviceMessageVM.getPort());
        m2mMessage.setIp(deviceMessageVM.getIp());
        m2mMessage.setImageData(deviceMessageVM.getIsImageData());
        //parseSensorSpecificData(m2mMessage,deviceMessageVM);
        
        m2mMessageRepository.save(m2mMessage);
		
		Sensor sensor = m2mMessage.getSensor();
        sensor.setLastMessage(m2mMessage.getHexMessage());
        sensor.setLastSeenDate(ZonedDateTime.now());
        
        sensorRepository.save(sensor);
        
        return m2mMessage;
	}

	@Override
	public void parseSensorSpecificData(M2mMessage m2mMessage, DeviceMessageVM deviceMessageVM) throws Exception {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void postProcess(M2mMessage m2mMessage) throws IOException {
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
	public void sendData(DeviceMessageVM deviceMessageVM,M2mMessage m2mMessage) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
