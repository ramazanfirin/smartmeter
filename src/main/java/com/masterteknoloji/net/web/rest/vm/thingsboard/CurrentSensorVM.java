package com.masterteknoloji.net.web.rest.vm.thingsboard;

public class CurrentSensorVM {
	
	private Float current;

	private Float totalEnergy;
	
	private String reason;
	
	private Float battery;
	
	private Float cableTemperature;

	public Float getCurrent() {
		return current;
	}

	public void setCurrent(Float current) {
		this.current = current;
	}

	public Float getTotalEnergy() {
		return totalEnergy;
	}

	public void setTotalEnergy(Float totalEnergy) {
		this.totalEnergy = totalEnergy;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Float getBattery() {
		return battery;
	}

	public void setBattery(Float battery) {
		this.battery = battery;
	}

	public Float getCableTemperature() {
		return cableTemperature;
	}

	public void setCableTemperature(Float cableTemperature) {
		this.cableTemperature = cableTemperature;
	}
	
	
}
