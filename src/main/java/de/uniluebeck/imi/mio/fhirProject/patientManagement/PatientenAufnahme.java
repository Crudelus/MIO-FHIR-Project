package de.uniluebeck.imi.mio.fhirProject.patientManagement;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterClassEnum;
import ca.uhn.fhir.model.dstu.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.IGenericClient;

public class PatientenAufnahme {

    public static void main(String[] args){   
	
    	FhirContext ctx = new FhirContext();
    	String serverBase = "http://fhirtest.uhn.ca/base";
    	IGenericClient client = ctx.newRestfulGenericClient(serverBase);
    	
    	
    	
	System.out.println("###### Der Patient wird aufgenommen:");
	System.out.println("###### 1. Patientendaten werden gesetzt...");
	
	PatientCreationParameters patParams = new PatientCreationParameters();
	patParams.firstName = "Karla";
	patParams.lastName = "Loeffel";
	patParams.maidenName = "Gabel";
	patParams.gender = AdministrativeGenderCodesEnum.F;
	patParams.birthDate = new DateTimeDt("1954-10-13");
	patParams.line = "Koenigstrasse 3";
	patParams.zip = "28193";
	patParams.city = "Dulldorf";
	patParams.maritalStatus = MaritalStatusCodesEnum.W;
	
	AdmissionParameters admParams = new AdmissionParameters();
	admParams.admissionClass = EncounterClassEnum.INPATIENT;
	admParams.diagnosisICD = "S06.0";
	admParams.diagnosisDescription = "Gehirnersch�tterung";
	
    	// creating a new Patient
//    	List<MyName> name = new ArrayList<MyName>(); name.add(new MyName(1,"Karl", "Loeffel","LOEFFEL, Karl"));
//    	AdministrativeGenderCodesEnum gender = AdministrativeGenderCodesEnum.M; // male
//    	DateTimeDt birthDate = new DateTimeDt("1976-02-11");
//    	List<MyAddress> address = new ArrayList<MyAddress>(); address.add(new MyAddress(1,"Koenigstrasse 3", "28193", "Dulldorf", "Deutschland"));
//    	MaritalStatusCodesEnum maritalStatus = MaritalStatusCodesEnum.W;
//    	EncounterClassEnum admClass = EncounterClassEnum.INPATIENT; //inpatient
//    	String diagnosisICD = "S06.0"; 	String diagnosisText = "Intrakranielle Verletzung - Gehirnersch�tterung";
    
    	
    	
	System.out.println("###### 2. ID des behandelnden Arzts wird abgerufen...");
    	// doctor that admits the patient
    	String docName = "HOUSE, Gregory";
	Bundle results = client
		.search()
		.forResource(Practitioner.class)
		.where(Practitioner.FAMILY.matches().value("House"))
		.and(Practitioner.GIVEN.matches().value("Gregory"))
		.execute();
	if (results.isEmpty()){
	    System.out.println("###### Es wurde kein Arzt mit diesen Merkmalen gefunden!");
	}else{
    		IdDt docId = results.getEntries().get(0).getId();
    		System.out.println("DocID: "+docId);
    		
        	//MyEntering(IGenericClient client,FhirContext ctx,List<MyName> name, int gender, DateTimeDt birthDate, List<MyAddress> address, int maritalStatus, int admClass, String diagnosisICD,String diagnosisText,Practitioner doctor)
//        	new MyEntering(client, ctx, name, gender, birthDate, address, maritalStatus, admClass, diagnosisICD, diagnosisText, docId, docName);
    		new MyEntering(client, ctx, patParams, admParams);
	}
	System.out.println("###### finished");
    }
    
    
    /**
     * Getter for patient ID
     * @param client
     * @param family
     * @param given
     * @return
     */
    public IdDt getPatId(IGenericClient client,String family, String given ){
	Bundle results = client
		.search()
		.forResource(Patient.class)
		.where(Patient.FAMILY.matches().value(family))
		.and(Patient.GIVEN.matches().value(given))
		.execute();
	return results.getEntries().get(0).getId();
    }
    
    
    
}
