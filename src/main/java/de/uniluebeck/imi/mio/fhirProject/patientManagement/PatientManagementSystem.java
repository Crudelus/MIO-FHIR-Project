package de.uniluebeck.imi.mio.fhirProject.patientManagement;

import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.rest.client.IGenericClient;

// Absolutely work-in-progress!
public class PatientManagementSystem implements IPatientManagementSystem{

	private FhirContext context;
	private IGenericClient client;
	
	public PatientManagementSystem(FhirContext inContext, IGenericClient inClient)
	{
		this.context = inContext;
		this.client = inClient;
		
		//TODO: Create default infrastructure
	}
	
	
	@Override
	public Organization getBirthStation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Organization getIMC() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Admission admitPatient(PatientCreationParameters patient,
			AdmissionParameters parameters) {
	    
	    return new MyEntering(client, ctx, patParams, admParams).getAdmission();
		// TODO Auto-generated method stub
//		return null;
	}

	@Override
	public List<Practitioner> getNurses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Patient> getPatientList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean dischargePatient(Admission admission) {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public boolean updatePatient(Patient patient) {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public boolean transferPatient(Patient patient, Organization targetStation,
			String duration) {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public List<Encounter> getAllPatientEncounters(Patient patient) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearEntries() {
		// TODO Auto-generated method stub
		
	}

}
