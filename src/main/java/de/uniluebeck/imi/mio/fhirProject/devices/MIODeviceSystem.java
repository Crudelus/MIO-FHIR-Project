/**
 * 
 */
package de.uniluebeck.imi.mio.fhirProject.devices;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Device;
import ca.uhn.fhir.model.dstu.resource.DeviceObservationReport;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;

/**
 * @author Daniel Rehmann, Simon Baumhof, Katharina Merkel
 * 
 */
public class MIODeviceSystem implements IDevice {
    FhirContext ctx;
    private ServerCommunication communicator;
    ResourceReferenceDt hospital;

    /**
     * @param serverBase
     * @param ctx
     * @param hospital 
     */
    public MIODeviceSystem(String serverBase, FhirContext ctx, ResourceReferenceDt hospital ) {
	this.ctx = ctx;
	communicator = new ServerCommunication(ctx,
		    serverBase, hospital);
	this.hospital=hospital;

    }

    public void createBasicInfrastructure() {
	// Location locToko = new Location().setName("Geburtsstation"); //TODO
	// location korrekt über Gruppe 2 abfragen
//	Device tokometer = new Device();
//	tokometer.setUdi("Tokometer2");
//	tokometer.setOwner(new ResourceReferenceDt(new IdDt("Organization","6010")));
//	communicator.createRessourceOnServer(tokometer);
//	communicator.createRessourceOnServer(tokometer);
//	Organization mio = new Organization().addIdentifier("Daniels", "1").setName("MIO-KH");
//	Organization gs = new Organization().addIdentifier("Daniels", "2").setName("Geburtsstation");
//	Organization hannes = new Organization().addIdentifier("Daniels", "3").setName("Hallo Hannes");
//	MethodOutcome returned = communicator.createRessourceOnServer(mio);
//	gs.setPartOf(new ResourceReferenceDt(returned.getId()));
//	returned=communicator.createRessourceOnServer(gs);
//	hannes.setPartOf(new ResourceReferenceDt(returned.getId()));
//	communicator.createRessourceOnServer(hannes);
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


    public boolean setPatient(IdDt devID, IdDt patID) {

	Device dev = communicator.getDevice(devID);
	dev.setPatient(new ResourceReferenceDt(patID));
	return communicator.updateDevice(dev);

    }

    @Override
    public boolean updateDeviceLocation(IdDt devID, IdDt locID) {
	Device dev = communicator.getDevice(devID);
	dev.setLocation(new ResourceReferenceDt(locID));
	return communicator.updateDevice(dev);
    }

    public void delAll() {
	communicator.deleteAll();
    }

    public boolean delDev(String id) {
	IdDt deviceId = new IdDt("Device", id);
	communicator.deleteDevice(deviceId);
	return true;
    }

    public ResourceReferenceDt getDeviceLocation(IdDt devId) {
	Device dev = communicator.getDevice(devId);
	return dev.getLocation();
    }

    /**
     * @param patId
     * @return
     */
    public ArrayList<DeviceObservationReport> getDeviceObservationReportsForPatient(
	    IdDt patId) {
    	
    	return communicator.getObservationForPatient(
		patId);


    }

    /**
     * @param obsRepForPat
     * @return
     */
    public ArrayList<DeviceAndTimeForPatient> getDeviceAndTimeForPatient(
	    ArrayList<DeviceObservationReport> obsRepForPat) {

	ArrayList<DeviceAndTimeForPatient> devicesForPat = new ArrayList<DeviceAndTimeForPatient>();

	for (DeviceObservationReport report : obsRepForPat) {

	    devicesForPat.add(new DeviceAndTimeForPatient(report.getInstant()
		    .getValue(), report.getSource().getReference()));
	}

	return devicesForPat;
    }

    public List<Device> getHospitalDevices() {
	List<Device> devices = communicator.getAllDevices();

	return devices;
    }

}
