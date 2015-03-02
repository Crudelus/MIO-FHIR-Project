package de.uniluebeck.imi.mio.fhirProject.patientManagement;

import java.util.List;

import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.model.primitive.IdDt;

public interface IPatientManagementSystem {
	
	/**
	 *  Get the id of the hospital organization
	 * @return
	 */
    public IdDt getHospitalID();
    
    /**
     * Plan the stay of a patient at the hospital.
     * @param patientParameters
     * @param admissionParameters
     * @return
     */
	public AdmissionContainer planAdmission(PatientCreationParameters patientParameters, 
		 						AdmissionParameters admissionParameters);	

	/**
	 * Always plan encounter before first admission.
	 * Only use for first treatment after planning.
	 * All later treatments are handled by transferal
	 * @param admission
	 * @param parameters
	 * @param targetOrganizationReference
	 * @param duration
	 * @return
	 */
	public AdmissionContainer admitPatient(AdmissionContainer admission,							
											AdmissionParameters parameters,
											ResourceReferenceDt targetOrganizationReference,
											long duration);
            
	/**
	 * Transfer a patient within the hospital. 
	 * Use for any treatments following the original admission.
	 * @param admission
	 * @param targetOrganizationReference
	 * @param duration
	 * @param diagnosisICD
	 * @param diagnosisDescription
	 * @return
	 */
	public boolean transferPatient(AdmissionContainer admission, 
									ResourceReferenceDt targetOrganizationReference,
									long duration,
									String diagnosisICD,
									String diagnosisDescription);
							
    /**
     *  Return list of all default nurse objects
     * @return
     */
    public List<Practitioner> getNurseList();
	
    /**
     *  Return list of all default doctor objects
     * @return
     */
    public List<Practitioner> getDoctorList();
    
	/**
	 *  Return list of all default patient objects 
	 * @return
	 */
	public List<Patient> getPatientList();
	
    /**
     *  Release a patient from the hospital, sending him home and terminating his stay   
     * @param admission
     * @return
     */
    public boolean dischargePatient(AdmissionContainer admission);
	
	/**
	 *  Update data of an existing patient with the input object
	 */
	public boolean updatePatient(Patient patient);	

	/**
	 * Get all known encounters for a given patient
	 * @param patient
	 * @return
	 */
	public List<Encounter> getAllPatientEncounters(Patient patient);
	
    /**
     *  Get relevant stations
     * @return
     */
    public Organization getBirthStation();    
    public Organization getIMC();
    public Organization getNICU();		
    
    /**
     *  Remove all infrastructure and default entries
     */
    public void clearEntries();	
}
