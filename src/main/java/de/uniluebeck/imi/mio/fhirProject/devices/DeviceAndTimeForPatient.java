package de.uniluebeck.imi.mio.fhirProject.devices;

import java.util.Date;

import ca.uhn.fhir.model.primitive.IdDt;

public class DeviceAndTimeForPatient {
	
	Date date;
	IdDt device;
	
	public DeviceAndTimeForPatient(Date date, IdDt device){
		this.date = date;
		this.device = device;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public IdDt getDevice() {
		return device;
	}

	public void setDevice(IdDt device) {
		this.device = device;
	}

}
