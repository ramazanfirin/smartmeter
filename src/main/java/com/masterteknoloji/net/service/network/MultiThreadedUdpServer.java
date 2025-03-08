package com.masterteknoloji.net.service.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.masterteknoloji.net.service.M2mMessageService;
import com.masterteknoloji.net.web.rest.UserResource;

import afu.org.checkerframework.checker.units.qual.s;

@Service
public class MultiThreadedUdpServer {
	
    private final Logger log = LoggerFactory.getLogger(MultiThreadedUdpServer.class);
	
    private static final int PORT = 5000;
    private static final int THREAD_POOL_SIZE = 10;
    private volatile boolean running = true;
    private DatagramSocket socket;
    private ExecutorService executorService;

    private final M2mMessageService m2mMessageService;
    
    Queue<ClientDto> queue = new LinkedList<>();
    
    public MultiThreadedUdpServer(M2mMessageService m2mMessageService) {
		super();
		this.m2mMessageService = m2mMessageService;
	}

	@PostConstruct
    public void startServer() {
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        executorService.execute(this::runServer);
    }

    @PreDestroy
    public void stopServer() {
        running = false;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        executorService.shutdown();
    }

    private void runServer() {
        try {
            socket = new DatagramSocket(PORT);
            log.info("UDP Sunucu " + PORT + " portunda dinliyor...");

            while (running) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                executorService.execute(() -> processPacket(packet));
            }
        } catch (Exception e) {
            if (running) e.printStackTrace();
        }
    }

    private void processPacket(DatagramPacket packet) {
        try {
            
        	InetAddress clientAddress = packet.getAddress();
            int clientPort = packet.getPort();
            String receivedMessage = new String(packet.getData(), 0, packet.getLength());

            log.info("[" + clientAddress.getHostAddress() + ":" + clientPort + "] Gelen Mesaj: " + receivedMessage);
            
            if(!receivedMessage.startsWith("P"))
            	sendPackage(packet);
            
            m2mMessageService.process(receivedMessage, Long.valueOf(clientPort),clientAddress.getHostAddress());
            
//            queue.add(new ClientDto(receivedMessage, Long.valueOf(clientPort),clientAddress.getHostAddress()));
//            if(queue.size()==4) {
//            	while (!queue.isEmpty()) {
//            		ClientDto clientDto = queue.poll();
//            		m2mMessageService.process(clientDto.getMessage(), clientDto.getPort(),clientDto.getIp());
//                }
//            }
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void sendPackage(DatagramPacket packet) throws IOException {
    	 String stringData = "0A01"; 
    	 byte[] data = stringData.getBytes();
    	 DatagramPacket sendPacket = new DatagramPacket(data, data.length, packet.getAddress(), packet.getPort());
    	 socket.send(sendPacket);
    }
}
