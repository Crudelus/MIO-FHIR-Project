package de.uniluebeck.imi.mio.fhirProject.interfaces;

import java.util.Vector;

import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Claim.Patient;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterClassEnum;
import ca.uhn.fhir.model.dstu.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import de.uniluebeck.imi.mio.fhirProject.patientManagement.AdmissionContainer;
import de.uniluebeck.imi.mio.fhirProject.patientManagement.IPatientManagementSystem;

public interface IObservation {
	/**
	 * create an Admission
	 */
	//public boolean createAdmission(EncounterClassEnum ECE, String diagnosisICD, String diagnosisDescription, IdDt doctorID,IdDt station,IdDt hospital,String firstName,String lastName,String maidenName,DateTimeDt birthDate,AdministrativeGenderCodesEnum gender,MaritalStatusCodesEnum maritalStatus,String line,String zip,String city);

	/**
	 * discharge a Patient 
	 */
	//public boolean dischargePatient(AdmissionContainer adCont);
	
	/**
	 * transfer of a patient 
	 */
	//public boolean transferPatient(AdmissionContainer admission, 
			//ResourceReferenceDt targetOrganizationReference,
			//long duration,
			//String diagnosisICD,
			//String diagnosisDescription);
	
	/**
	 * create Birth
	 */
	//public boolean createBirth();
	
	/**
	 * change Maritial Status 
	 */
	//public boolean changeMaritialStatus(Patient patient, MaritalStatusCodesEnum mariage);
	
	/**
	 * Liefert Übersicht über die Stationen 
	 */
	public Vector<String> getStations( );
	
	/**
	 * liefert Übersicht über die eingewiesenden Patienten 
	 */
	public Vector<String> getPatients();
}
