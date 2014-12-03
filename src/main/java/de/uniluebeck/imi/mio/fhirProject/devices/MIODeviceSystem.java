/**
 * 
 */
package de.uniluebeck.imi.mio.fhirProject.devices;

import java.util.List;

import javax.naming.Reference;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Device;
import ca.uhn.fhir.model.dstu.resource.Location;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.IDelete;

/**
 * @author Daniel Rehmann, Simon Baumhof
 *
 */
public class MIODeviceSystem implements IDevice {
	private String serverBase = "http://fhirtest.uhn.ca/base";
	FhirContext ctx = new FhirContext(); // TODO should be the same in every
											// class;
	private ServerCommunication communicator = new ServerCommunication(ctx,
			serverBase);

	public void createBasicInfrastructure() {
		// Location locToko = new Location().setName("Geburtsstation"); //TODO
		// location korrekt über Gruppe 2 abfragen
		Device tokometer = new Device();
		tokometer.setUdi("Tokometer1");
		communicator.createDeviceOnServer(tokometer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniluebeck.imi.mio.fhirProject.devices.IDevice#getDevice(java.lang
	 * .String)
	 */
	@Override
	public Device getDevice(String udi) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniluebeck.imi.mio.fhirProject.devices.IDevice#getDevice(ca.uhn.fhir
	 * .model.primitive.IdDt)
	 */
	@Override
	public Device getDevice(IdDt deviceId) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniluebeck.imi.mio.fhirProject.devices.IDevice#getDeviceForPatient
	 * (ca.uhn.fhir.model.primitive.IdDt)
	 */
	@Override
	public List<Device> getDeviceForPatient(IdDt patId) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniluebeck.imi.mio.fhirProject.devices.IDevice#getObservationsForPatient
	 * (ca.uhn.fhir.model.primitive.IdDt)
	 */
	@Override
	public List<Observation> getObservationsForPatient(IdDt patId) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniluebeck.imi.mio.fhirProject.devices.IDevice#PatientHasDevice(ca
	 * .uhn.fhir.model.primitive.IdDt)
	 */
	@Override
	public boolean PatientHasDevice(IdDt patId) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setServerBase(String base) {
		this.serverBase = base;
	}

	@Override
	public boolean updateDeviceLocation(IdDt devID, IdDt locID) {
		Device dev = communicator.getDevice(devID);
		dev.setLocation(new ResourceReferenceDt(locID));
		return communicator.updateDevice(dev);
	}
}
