
package de.uniluebeck.imi.mio.fhirProject.observation;

import java.util.List;
import java.util.Vector;

import de.uniluebeck.imi.mio.fhirProject.App;
import de.uniluebeck.imi.mio.fhirProject.patientManagement.AdmissionParameters;
import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
//import ca.uhn.fhir.model.dstu.resource.Claim.Patient;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterClassEnum;
import ca.uhn.fhir.model.dstu.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.IGenericClient;
import de.uniluebeck.imi.mio.fhirProject.patientManagement.AdmissionContainer;
import de.uniluebeck.imi.mio.fhirProject.interfaces.IObservation;
import de.uniluebeck.imi.mio.fhirProject.patientManagement.IPatientManagementSystem;
import de.uniluebeck.imi.mio.fhirProject.patientManagement.PatientCreationParameters;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;

public class Observations implements IObservation {
	
	IPatientManagementSystem patientManagement;
	IGenericClient client = App.ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/base");
	
	public Observations(IPatientManagementSystem pMan){
		patientManagement = pMan;
	}
/*
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
										MaritalStatusCodesEnum mariage) 
	{
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
	*/
	
	 public Vector<String> getPatients() { 
		 Bundle results = client.search().forResource(Patient.class).execute();
		 List<Patient> list = results.getResources(Patient.class);
		 Vector<String> vec = new Vector<String>();
		 for(Patient p : list)
		 {
			 for(HumanNameDt name : p.getName())
			 {
				 if(!(name.getPrefixAsSingleString() == null)) 
				 { 
					 vec.add(name.getPrefixAsSingleString());
				 } 
				 else
				 { 
					 vec.add("");
				 } 
				 
				 if(!(name.getFamilyAsSingleString()==null))
				 { 
					 vec.add(name.getFamilyAsSingleString()); 
				 }
				 else
				 { 
					 vec.add("");
				 }
				 
				 if(!(name.getGivenAsSingleString()==null))
				 {
					 vec.add(name.getGivenAsSingleString()); 
			     }
				 else
			     {
					 vec.add("");
				 }
			 } 
			 
			 for(AddressDt adress : p.getAddress()) { System.out.println(adress.getCountry().getValueAsString() == null); if(! (adress.getCountry().getValue() == null)) { vec.add(adress.getCountry().getValueAsString()); }else { vec.add(""); } if(!(adress.getCity().getValue()==null)) { vec.add(adress.getCity().getValueAsString()); } else { vec.add(""); } if(!(adress.getZip().getValue()==null)) { vec.add(adress.getZip().getValueAsString()); }else { vec.add(""); } if(!(adress.getLineFirstRep().getValueAsString()==null)) { vec.add(adress.getLineFirstRep().getValueAsString()); } else { vec.add(""); } } }
		 
		 
		 return vec; }
	
	
	public Vector<String> getStations()
	{
		
		return null;
	}
}
