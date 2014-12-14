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
    IdDt hospital;

    /**
     * @param serverBase
     * @param ctx
     * @param hospital 
     */
    public MIODeviceSystem(String serverBase, FhirContext ctx, IdDt hospital ) {
	this.ctx = ctx;
	communicator = new ServerCommunication(ctx,
		    serverBase, hospital);
	this.hospital=hospital;
    }

    public void createBasicInfrastructure() {
	Device tokometer = new Device();
	tokometer.setUdi("toko1");
	Organization birthstation = communicator.getStation("Geburtsstation"); // Birthstation im original Datensatz
	tokometer.setOwner(new ResourceReferenceDt(birthstation)); //Get via infrastructure 
	System.out.println("");
	communicator.createRessourceOnServer(tokometer);
// 	ResourceReferenceDt bs01 = birthstation.getLocation().get(0);
//	tokometer.setLocation(bs01);    //Get via infrastructure 	
//	communicator.createRessourceOnServer(tokometer); //nach diesem Schema alle geräte erstellen
	
	
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

    /**
     * @param devId
     * @return ResourceReferenceDt for the Location
     */
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
     * @return {@link ArrayList} of {@link Device} containing all devices for Patient
     */
    public ArrayList<DeviceAndTimeForPatient> getDeviceAndTimeForPatient(
	    ArrayList<DeviceObservationReport> obsRepForPat) {

	ArrayList<DeviceAndTimeForPatient> devicesForPat = new ArrayList<>();

	for (DeviceObservationReport report : obsRepForPat) {

	    devicesForPat.add(new DeviceAndTimeForPatient(report.getInstant()
		    .getValue(), report.getSource().getReference()));
	}

	return devicesForPat;
    }

    /**
     * @return all devices for Patient
     */
    public List<Device> getHospitalDevices() {
	List<Device> devices = communicator.getAllDevices();

	return devices;
    }
}
