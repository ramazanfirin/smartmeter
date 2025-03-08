package com.masterteknoloji.net.service.network;

public class ClientDto {
	
	String message;
	Long port;
	String ip;
	
	
	public ClientDto(String message, Long port,String ip) {
		super();
		this.message = message;
		this.port = port;
		this.ip = ip;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getPort() {
		return port;
	}
	public void setPort(Long port) {
		this.port = port;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	

}
