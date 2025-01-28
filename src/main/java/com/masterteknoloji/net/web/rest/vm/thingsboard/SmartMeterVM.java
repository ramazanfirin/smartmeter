package com.masterteknoloji.net.web.rest.vm.thingsboard;

public class SmartMeterVM {
	
	private Float readingValue;
	
	public SmartMeterVM(Float readingValue) {
		super();
		this.readingValue = readingValue;
	}

	public Float getReadingValue() {
		return readingValue;
	}

	public void setReadingValue(Float readingValue) {
		this.readingValue = readingValue;
	}

}
