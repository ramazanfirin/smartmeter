package com.masterteknoloji.net.service.mqtt;

import java.math.BigInteger;

import javax.annotation.PostConstruct;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.masterteknoloji.net.config.ApplicationProperties;
import com.masterteknoloji.net.service.M2mMessageService;

@Service
public class MqttClientService implements MqttCallback {
	
	private final Logger log = LoggerFactory.getLogger(MqttClientService.class);

    private MqttClient mqttClient;

    private final ApplicationProperties applicationProperties;
    
    private final M2mMessageService m2mMessageService;

    public MqttClientService(ApplicationProperties applicationProperties, M2mMessageService m2mMessageService) {
		super();
		this.applicationProperties = applicationProperties;
		this.m2mMessageService = m2mMessageService;
	}

	@PostConstruct
    public void init() {
       // connect();
    }

    private void connect() {
        try {
        	mqttClient = new MqttClient(applicationProperties.getMqttBrokerUrl(), "Server", new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setAutomaticReconnect(true);
            options.setConnectionTimeout(10);
            options.setUserName(applicationProperties.getMqttUsername());
            options.setPassword(applicationProperties.getMqttPassword().toCharArray());
            mqttClient.setCallback(this);
            mqttClient.connect(options);
            mqttClient.subscribe("device/sub/+");


            log.info("Mqtt Bağlantı kuruldu.");
        } catch (MqttException e) {
        	log.info("Mqtt Bağlantı başarısız: " + e.getMessage());
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
    	log.info("Mqtt Bağlantı kaybedildi: " + cause.getMessage());
        reconnect();
    }

    private void reconnect() {
        while (true) {
            try {
            	log.info("Mqtt Bağlantı yeniden kuruluyor...");
                if (mqttClient != null && mqttClient.isConnected()) {
                    System.out.println("Zaten bağlı.");
                    return;
                }
                mqttClient.close(); // Eski bağlantıyı tamamen kapat
                connect(); // Yeni bağlantıyı başlat
                if (mqttClient != null && mqttClient.isConnected()) {
                	log.info("Mqtt Bağlantı başarıyla kuruldu.");
                }else {
                	log.info("Mqtt Bağlantı başarısız.");
                }
                return;
            } catch (Exception e) {
            	log.info("Mqtt Yeniden bağlantı başarısız, 5 saniye sonra tekrar denenecek.");
                try {
                    Thread.sleep(5000);  // 5 saniye bekle ve tekrar dene
                } catch (InterruptedException ignored) {}
            }
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        
    	String receivedMessage =  new String(message.getPayload());
    	String imei = topic.replace("device/sub/", "");
    	log.info("Mqtt mesaj alındı. IMEI: " + imei + " Mesaj: " + receivedMessage);
        try {
        	if(!receivedMessage.startsWith("P"))
            	sendGetImageCommand(imei);
        	
			m2mMessageService.process("MQTT",imei,receivedMessage, 0l,"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw new RuntimeException();
		}
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    	//log.info("Mesaj başarıyla gönderildi.");
    }

    public void sendGetImageCommand(String imei) {
//    	 try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	publish(imei, "0A01");
    }
    
    public void publish(String topic, String hexString) {
    	try {
            MqttMessage mqttMessage = new MqttMessage(hexString.getBytes());
            
            //byte[] payload = new BigInteger(hexString, 16).toByteArray();
            //MqttMessage mqttMessage = new MqttMessage(payload);
            
            mqttMessage.setQos(1);
            mqttMessage.setRetained(true);
            if(mqttClient.isConnected()) {
            	mqttClient.publish("device/"+topic, mqttMessage);
            	log.info("Mesaj gönderildi: Mesaj:" + hexString+",topic:"+topic);
            }
            } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    
   // @Scheduled(fixedRate = 15000)
    public void publishTest() {
    	publish("device/sub/aaaa", "merhaba");
    }
    
	public MqttClient getMqttClient() {
		return mqttClient;
	}

	public void setMqttClient(MqttClient mqttClient) {
		this.mqttClient = mqttClient;
	}
}
