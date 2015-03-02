/**
 * 
 */
package de.uniluebeck.imi.mio.fhirProject.devices;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Device;
import ca.uhn.fhir.model.dstu.resource.DeviceObservationReport;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.primitive.IdDt;

/**
 * @author Daniel Rehmann
 *
 */
public interface IDevice {

    
    /**
     * @param deviceId reference ID for device
     * @return a ca.uhn.fhir.model.dstu.resource.Device or null if Device not found on server
     */
    public Device getDevice(IdDt deviceId);
    
    /**
     * Returns the device's location
     * 
     * @param devId
     * @return a ca.uhn.fhir.model.dstu.resource.Location or null if Location not found on server
     */
    public ResourceReferenceDt getDeviceLocation(IdDt devId);
    
	/**
	 * @return all devices of the hospital
	 */
    public List<Device> getHospitalDevices();
    
    /**
     * Creates an overview of Devices that were used on the Patient and the time of use.
     * 
     * @param patId - The Patient's unique ID
     * @return ArrayList<DeviceAndTimeForPatient> - ArrayList of objects containing the Device and Time...
     */
    public ArrayList<DeviceAndTimeForPatient> getDeviceAndTimeForPatient(IdDt patId);
 
    /**
     * @param devID reference ID for device
     * @param locID reference ID for location
     * @return true if updated, else false
     */
    public boolean updateDeviceLocation(IdDt devID, IdDt locID);
    
	/**
	 * Creates a DiagnosticReport containing laboratory data
	 * 
	 * @param diagnosticOrderId
	 * @param patId
	 * @param performerId
	 * @return IdDt of the created DiagnosticReport
	 */
	public IdDt newLaboratoryReport(IdDt diagnosticOrderId, IdDt patId, IdDt performerId);
	
	/**
	 * Creates a DeviceObservationReport containing some data
	 * 
	 * @param diagnosticOrderId
	 * @param patId
	 * @param deviceId
	 * @return IdDt of the created DeviceObservationReport
	 */
	public IdDt newDeviceObservationReport(IdDt diagnosticOrderId, IdDt patId, IdDt deviceId);
	
	/**
	 * Deletes ALL Entities created from MIODevSys
	 */
	public void delAll();
	
	/**
	 * @param patId
	 * @return All DeviceObservationReports for the Patient
	 */
	public ArrayList<DeviceObservationReport> getDeviceObservationReportsForPatient(IdDt patId);
}
