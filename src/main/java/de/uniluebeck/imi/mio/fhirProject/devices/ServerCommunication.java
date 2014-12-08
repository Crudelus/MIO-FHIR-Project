package de.uniluebeck.imi.mio.fhirProject.devices;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.ContainedDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Device;
import ca.uhn.fhir.model.dstu.resource.DeviceObservationReport;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.IQuery;

public class ServerCommunication {
    private IGenericClient client;
    private ArrayList<IdDt> generatedObjects = new ArrayList<IdDt>();
    ResourceReferenceDt hospitalId;

    public ServerCommunication(FhirContext ctx, String serverBase,
	    ResourceReferenceDt hospital) {
	this.client = ctx.newRestfulGenericClient(serverBase);
	this.hospitalId = hospital;
    }

    /**
     * @param resource
     *            the Resource that will be created
     * @return MethodOutcome for create Method, to get ID generated by the
     *         server use .getID()
     */
    public MethodOutcome createRessourceOnServer(IResource resource) {
	MethodOutcome outcome = client.create().resource(resource)
		.prettyPrint().encodedJson().execute();

	generatedObjects.add(outcome.getId());
	return outcome;
    }

    /**
     * @param devID
     *            the reference for the device
     * @return the device
     */
    public Device getDevice(IdDt devID) {
	return client.read(Device.class, devID);
    }

    /**
     * @param device
     *            the updated device
     * @return true if updated, else false
     */
    public boolean updateDevice(Device device) {
	try {
	    client.update().resource(device).execute();
	    return true;
	} catch (Exception e) {
	    System.err.println("update failed");
	    e.printStackTrace();
	    return false;
	}
    }

    /**
     * @param deviceID
     * @return true if deleted false if error
     */
    public boolean deleteDevice(IdDt deviceID) {
	try {
	    OperationOutcome outcome = client.delete().resourceById(deviceID)
		    .execute();
	    return true;
	} catch (Exception e) {
	    System.err.println("delete failed");
	    e.printStackTrace();
	    return false;
	}
    }

    public void deleteAll() {
	for (IdDt id : generatedObjects) {

	    deleteDevice(id);
	}
    }

    public ArrayList<DeviceObservationReport> getObservationForPatient(IdDt patId) {
    	
    	ArrayList<DeviceObservationReport> obsForPat = new ArrayList<>();
    	
    	Bundle bundle = client.search().forResource(DeviceObservationReport.class)
		.where(DeviceObservationReport.SUBJECT.hasId(patId)).execute();
    	
    	while(!bundle.getLinkNext().isEmpty()){
		    bundle = client.loadPage().next(bundle).execute();
		    obsForPat.addAll(bundle.getResources(DeviceObservationReport.class));
		}	
	return obsForPat;
    }

    /**
     * @return
     */
    public List<Device> getAllDevices() {
	Organization org = client.read(Organization.class,
		hospitalId.getReference());

	System.out.println(org.getId());
	
	ArrayList<Organization> toCheck = new ArrayList<>();
	ArrayList<Organization> partOfHospital = new ArrayList<>();
	
	toCheck.add(org);
	
	while (!toCheck.isEmpty()){
	    ArrayList<Organization> temp= new ArrayList<Organization>();
	    for(Organization inHospital : toCheck){
	    	
		Bundle bundle=client.search().forResource(Organization.class).where(Organization.PARTOF.hasId(inHospital.getId())).execute();
		temp.addAll(bundle.getResources(Organization.class));
		
		
		
		while(!bundle.getLinkNext().isEmpty()){
		    bundle = client.loadPage().next(bundle).execute();
		    temp.addAll(bundle.getResources(Organization.class));
		}		
	    }
	    partOfHospital.addAll(toCheck);
	    System.out.println(partOfHospital.size());
	    toCheck.clear();
	    toCheck.addAll(temp);
	    toCheck.removeAll(partOfHospital);	    
	}
	
	
	//DIE GANZE SCHEI?E FUNZT NICHT !!!!!!!!!!einself!
	return null;
	
	
    }

}
