package com.masterteknoloji.net.service.network;

public class ClientDto {
	
	String message;
	Long port;
	
	
	public ClientDto(String message, Long port) {
		super();
		this.message = message;
		this.port = port;
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
	

}
