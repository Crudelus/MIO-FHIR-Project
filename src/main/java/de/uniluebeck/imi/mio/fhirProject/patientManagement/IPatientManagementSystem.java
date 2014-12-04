package patientManagement;

import java.util.List;

import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Practitioner;

public interface IPatientManagementSystem {
	
    // Plan encounters
    public boolean planEncounter(ResourceReferenceDt patientReference, String diagnosisICD, ResourceReferenceDt stationReference);
	
	  // Get the two relevant stations [references?]
    public Organization getBirthStation();
    public Organization getIMC();

	// Admit a new patient:
    //  -Creates patient according to parameters if necessary
    //  -Creates main Encounter and Hospitalization for patient
	public AdmissionContainer admitPatient(PatientCreationParameters patient, AdmissionParameters parameters);	

    // Get all nurses [references?]
    public List<Practitioner> getNurses();
	
	// Get all patients [references?]
	public List<Patient> getPatientList();
	
    // Release a patient from the hospital   
    public boolean dischargePatient(AdmissionContainer admission);
	
	// Update data of an existing patient
	public boolean updatePatient(Patient patient);
	
	// Transfer a patient to a new station
	public boolean transferPatient(AdmissionContainer admission, 
			ResourceReferenceDt targetOrganizationReference,
			long duration,
			String diagnosisICD,
			String diagnosisDescription);	

    // Get all previous main encounters for a patient
    public List<Encounter> getAllPatientEncounters(Patient patient);
    

    
    // Remove all infrastructure and default entries
    public void clearEntries();	
}
