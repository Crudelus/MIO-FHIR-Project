package de.uniluebeck.imi.mio.fhirProject.observation;

import de.uniluebeck.imi.mio.fhirProject.patientManagement.AdmissionParameters;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Claim.Patient;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterClassEnum;
import ca.uhn.fhir.model.dstu.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import de.uniluebeck.imi.mio.fhirProject.patientManagement.AdmissionContainer;
import de.uniluebeck.imi.mio.fhirProject.interfaces.IObservation;
import de.uniluebeck.imi.mio.fhirProject.patientManagement.IPatientManagementSystem;
import de.uniluebeck.imi.mio.fhirProject.patientManagement.PatientCreationParameters;

public class Observations implements IObservation {
	IPatientManagementSystem patientManagement;

	public Observations(IPatientManagementSystem pMan){
		patientManagement = pMan;
	}
	
	public boolean createAdmission(EncounterClassEnum ECE, String diagnosisICD,
			String diagnosisDescription, IdDt doctorID, IdDt station,
			IdDt hospital, String firstName, String lastName,
			String maidenName, DateTimeDt birthDate,
			AdministrativeGenderCodesEnum gender,
			MaritalStatusCodesEnum maritalStatus, String line, String zip,
			String city ) {
		// TODO Auto-generated method stub
		PatientCreationParameters patientParameters = new PatientCreationParameters();
		patientParameters.birthDate = birthDate;
		patientParameters.firstName = firstName;
		patientParameters.lastName = lastName;
		patientParameters.maidenName = maidenName;
		patientParameters.city = city;
		patientParameters.line = line;
		patientParameters.zip = zip;
		patientParameters.gender = gender;
		patientParameters.maritalStatus = maritalStatus;
		
		AdmissionParameters admitParameter = new AdmissionParameters(); 
		admitParameter.admissionClass =  ECE;
		admitParameter.diagnosisICD = diagnosisICD;
		admitParameter.diagnosisDescription = diagnosisDescription;
		admitParameter.doctorID = doctorID;
		admitParameter.station = station;
		admitParameter.hospital = hospital;
		
		
		AdmissionContainer scenarioAdmission = patientManagement.admitPatient(patientParameters, admitParameter);
		
		if (scenarioAdmission != null){
			return true; 
		}
		else {
			return false;
		}
		
	}

	public boolean createBirth() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean changeMaritialStatus(Patient patient,
			MaritalStatusCodesEnum mariage) {
		// TODO Auto-generated method stub
		patient.setMaritalStatus(mariage);
		
		return true;
	}

	public boolean dischargePatient(AdmissionContainer adCont) {
		// TODO Auto-generated method stub
		patientManagement.dischargePatient(adCont);
		return false;
	}

	
	public boolean transferPatient(AdmissionContainer admission,
			ResourceReferenceDt targetOrganizationReference, long duration,
			String diagnosisICD, String diagnosisDescription) {
		// TODO Auto-generated method stub
		ResourceReferenceDt imcReference = new ResourceReferenceDt(patientManagement.getIMC());
		 
		patientManagement.transferPatient(admission, imcReference, duration, diagnosisICD, diagnosisDescription);
		
		return true;
	}

}
