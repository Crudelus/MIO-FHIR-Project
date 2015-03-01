package de.uniluebeck.imi.mio.fhirProject.patientManagement;

import java.util.List;

import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.model.primitive.IdDt;

public interface IPatientManagementSystem {
	
    public IdDt getHospitalID();
    
    // Plan encounters
	public AdmissionContainer planAdmission(PatientCreationParameters patientParameters, 
		 						AdmissionParameters admissionParameters);	

	// Admit a new patient:
    //  -Creates patient according to parameters if necessary
    //  -Creates main Encounter and Hospitalization for patient
	public AdmissionContainer admitPatient(AdmissionContainer admission,							
											AdmissionParameters parameters,
											ResourceReferenceDt targetOrganizationReference,
											long duration);
            
	// Transfer a patient to a new station
	public boolean transferPatient(AdmissionContainer admission, 
									ResourceReferenceDt targetOrganizationReference,
									long duration,
									String diagnosisICD,
									String diagnosisDescription);
							
    // Return list of all default nurse objects
    public List<Practitioner> getNurseList();
	
    // Return list of all default doctor objects
    public List<Practitioner> getDoctorList();
    
	// Return list of all default patient objects 
	public List<Patient> getPatientList();
	
    // Release a patient from the hospital   
    public boolean dischargePatient(AdmissionContainer admission);
	
	// Update data of an existing patient
	public boolean updatePatient(Patient patient);	

    // Get the two relevant stations [references?]
    public Organization getBirthStation();
    public Organization getIMC();

    
    // Remove all infrastructure and default entries
    public void clearEntries();	
}
