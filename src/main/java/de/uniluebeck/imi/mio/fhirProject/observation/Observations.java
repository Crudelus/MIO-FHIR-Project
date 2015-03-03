
package de.uniluebeck.imi.mio.fhirProject.observation;

import java.util.List;

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
	
	public String[][] getPatients(){
		Bundle results = client.search().forResource(Patient.class).execute();
		List<Patient> list = results.getResources(Patient.class);
		
		int size = list.size();
		String[][] PatArr = new String[size*14][2];
		
		int iterator = 0;
		
		for (Patient p : list) {
			
			
			for (HumanNameDt name : p.getName()) {
				
				PatArr[iterator][1] = name.getPrefixAsSingleString();
				PatArr[iterator][0] = "Titel";
								
				PatArr[iterator+1][1] = name.getFamilyAsSingleString();
				PatArr[iterator+1][0] = "Nachname";
				
				PatArr[iterator+2][1] = name.getGivenAsSingleString();	
				PatArr[iterator+2][0] = "Vorname";
				
				PatArr[iterator+3][1] = "";	
				PatArr[iterator+3][0] = "Geburtsname";
				
				PatArr[iterator+4][1] = name.getSuffixAsSingleString();
				PatArr[iterator+4][0] = "Zusatz";
			}
			
			DateTimeDt date = p.getBirthDate();
			date.getValueAsString();
			
			PatArr[iterator+5][1] = date.getValueAsString();
			PatArr[iterator+5][0] = "Geburtsdatum";
			
			for(AddressDt address : p.getAddress() ){
				//String street = address.getLine().get(0).getValueAsString();
				String street = address.getLineFirstRep().getValueAsString();
				String postalCode = address.getZip().getValueAsString();
				String city = address.getCity().getValueAsString();
				
				PatArr[iterator+6][1] = street+postalCode+city;
				PatArr[iterator+6][0] = "Adresse";
			}
			
			//p.getManagingOrganization().getResource().
			p.PROVIDER.getParamName();
			
			
			iterator=iterator+14;
		}
		
		return PatArr ; 
	}
	
	
	public String[][] getStations(){
		
		return null;
	}
}
