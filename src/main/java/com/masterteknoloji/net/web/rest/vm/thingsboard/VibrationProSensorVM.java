package com.masterteknoloji.net.web.rest.vm.thingsboard;

public class VibrationProSensorVM {
	
	private Float xVelocity;

	private Float xAcceleration;
	
	private Float yVelocity;
	
	private Float yAcceleration;
	
	private Float zVelocity;
	
	private Float zAcceleration;
	
	private Float temperature;
	
	private Float voltage;
	

	public VibrationProSensorVM() {
		super();
	}


	public Float getxVelocity() {
		return xVelocity;
	}


	public void setxVelocity(Float xVelocity) {
		this.xVelocity = xVelocity;
	}


	public Float getxAcceleration() {
		return xAcceleration;
	}


	public void setxAcceleration(Float xAcceleration) {
		this.xAcceleration = xAcceleration;
	}


	public Float getyVelocity() {
		return yVelocity;
	}


	public void setyVelocity(Float yVelocity) {
		this.yVelocity = yVelocity;
	}


	public Float getyAcceleration() {
		return yAcceleration;
	}


	public void setyAcceleration(Float yAcceleration) {
		this.yAcceleration = yAcceleration;
	}


	public Float getzVelocity() {
		return zVelocity;
	}


	public void setzVelocity(Float zVelocity) {
		this.zVelocity = zVelocity;
	}


	public Float getzAcceleration() {
		return zAcceleration;
	}


	public void setzAcceleration(Float zAcceleration) {
		this.zAcceleration = zAcceleration;
	}


	public Float getTemperature() {
		return temperature;
	}


	public void setTemperature(Float temperature) {
		this.temperature = temperature;
	}


	public Float getVoltage() {
		return voltage;
	}


	public void setVoltage(Float voltage) {
		this.voltage = voltage;
	}

	
}
