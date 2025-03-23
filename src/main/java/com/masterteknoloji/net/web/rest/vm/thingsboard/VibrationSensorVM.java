package com.masterteknoloji.net.web.rest.vm.thingsboard;

public class VibrationSensorVM {
	
	private Float xAxisValue;

	private Float yAxisValue;
	
	private Float zAxisValue;
	
	private Float temperature;
	
	

	public VibrationSensorVM(Float xAxisValue, Float yAxisValue, Float zAxisValue, Float temperature) {
		super();
		this.xAxisValue = xAxisValue;
		this.yAxisValue = yAxisValue;
		this.zAxisValue = zAxisValue;
		this.temperature = temperature;
	}

	public Float getxAxisValue() {
		return xAxisValue;
	}

	public void setxAxisValue(Float xAxisValue) {
		this.xAxisValue = xAxisValue;
	}

	public Float getyAxisValue() {
		return yAxisValue;
	}

	public void setyAxisValue(Float yAxisValue) {
		this.yAxisValue = yAxisValue;
	}

	public Float getzAxisValue() {
		return zAxisValue;
	}

	public void setzAxisValue(Float zAxisValue) {
		this.zAxisValue = zAxisValue;
	}

	public Float getTemperature() {
		return temperature;
	}

	public void setTemperature(Float temperature) {
		this.temperature = temperature;
	}
}
