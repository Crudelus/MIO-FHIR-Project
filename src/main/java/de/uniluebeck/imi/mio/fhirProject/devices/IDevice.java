/**
 * 
 */
package de.uniluebeck.imi.mio.fhirProject.devices;

import java.util.List;

import ca.uhn.fhir.model.dstu.resource.Device;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.primitive.IdDt;

/**
 * @author Daniel Rehmann
 *
 */
public interface IDevice {

    /**
     * @param udi FDA mandated unique Device ID
     * @return a ca.uhn.fhir.model.dstu.resource.Device 
     */
    public Device getDevice(String udi);
    
    
    /**
     * @param deviceId reference ID for device
     * @return a ca.uhn.fhir.model.dstu.resource.Device
     */
    public Device getDevice(IdDt deviceId);
    
    /**
     * @param patId reference to a Patient the device is affixed to;
     * @return A list of Devices for this Patient, can be empty
     */
    public List<Device> getDeviceForPatient(IdDt patId);
    
    
    /**
     * @param patId reference to a Patient
     * @return A list of Observations for this Patients, can be empty
     */
    public List<Observation> getObservationsForPatient(IdDt patId);  
    
    /**
     * @param patId reference to a Patient
     * @return true if Patient has Devices affixed to, else false
     */
    public boolean PatientHasDevice(IdDt patId);
    
    /**
     * @param devID reference ID for device
     * @param locID reference ID for location
     * @return true if updated, else false
     */
    public boolean updateDeviceLocation(IdDt devID, IdDt locID);
    
}
