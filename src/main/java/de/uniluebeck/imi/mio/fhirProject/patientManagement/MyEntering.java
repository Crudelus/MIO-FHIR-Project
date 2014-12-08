package patientManagement;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Condition;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.IGenericClient;

/**
 * Class of the main Admission:
 * putting Patient, Indication and admission together
 * gets input for creating a patient, the class of admission, the admission diagnosis (ICD+description), admitting doctor
 * @author Anne
 *
 */
public class MyEntering {
    
    
    private AdmissionContainer admission = new AdmissionContainer();
    /**
     * Constructor of the main admission
     * @param client
     * @param ctx
     * @param name
     * @param gender
     * @param birthDate
     * @param address
     * @param maritalStatus
     * @param admClass
     * @param diagnosisICD
     * @param diagnosisText
     * @param docId
     * @param docName
     */
//    public MyEntering(IGenericClient client,FhirContext ctx,
//			List<MyName> name, 
//			AdministrativeGenderCodesEnum gender, 
//	    		DateTimeDt birthDate, 
//	    		List<MyAddress> address, 
//	    		MaritalStatusCodesEnum maritalStatus, 
//	    		EncounterClassEnum admClass, 
//	    		String diagnosisICD,
//	    		String diagnosisText,
//	    		IdDt docId,
//	    		String docName) {
    
  public MyEntering(IGenericClient client,FhirContext ctx,
			PatientCreationParameters patParams,
	    		AdmissionParameters admParams) {
	
      
	System.out.println("###### 3. Die Aufnahme wird generiert...\n");
	System.out.println("###### 3.1. Existiert dieser Patient bereits?");
	Bundle results = client
		.search()
		.forResource(Patient.class)
		.where(Patient.GIVEN.matches().value(patParams.firstName))
		.and(Patient.FAMILY.matches().value(patParams.lastName))
		.execute();
	if(results.isEmpty()){
	    System.out.println("###### 3.2. Nein. Ein neuer Patient wird angelegt...");
	    boolean reAdmission = false;
//	    MyPatient patient = new MyPatient(name, gender, birthDate, address, maritalStatus);
	    MyPatient patient = new MyPatient(patParams);
	    patient.setCareProvider(new ResourceReferenceDt());
	    patient.setManagingOrganization(new ResourceReferenceDt());
		
	    Patient p = patient.getPatientObj(); 
	    IdDt patId = MyPatient.createPatient(client, p); //has to be uploaded first to get the Id for Encounter etc.
	    MyPatient.updatePatient(client, p);
	    

//	    printPat(ctx,p);
//	    Condition indication = new MyCondition(client, p, docID, docName, diagnosisICD, diagnosisText).getCondObj();
	    Condition indication = new MyCondition(client, p, admParams.doctorID, admParams.diagnosisICD, admParams.diagnosisDescription).getCondObj();
	    MyAdmission adm = new MyAdmission(client, p, admParams.admissionClass, indication.getId(),admParams.doctorID,admParams.station,admParams.hospital, reAdmission);
	    indication.setEncounter(new ResourceReferenceDt(adm.getAdmObj().getId()));
	    
	    // filling container class
	    admission.adm = adm.getAdmObj();
	    admission.visit = adm.getVisitObj();
	    admission.patient = p;
	    admission.hospitalization = adm.getHospitalization();
	    admission.condition = indication;
	}
	else{
	    System.out.println("###### 3.2. Ja. Die Patienten-ID wird abgerufen...");
	    boolean reAdmission = true;
	    IdDt patId = results.getEntries().get(0).getId();
	    Patient readmissionedPatient = getPatientFromID(client, patId);
	    System.out.println("PatID: "+patId);

	    Condition indication = new MyCondition(client, readmissionedPatient, admParams.doctorID, admParams.diagnosisICD, admParams.diagnosisDescription).getCondObj();
	    System.out.println("###### admission loaded");
	    MyAdmission adm = new MyAdmission(client, readmissionedPatient, admParams.admissionClass, indication.getId(),admParams.doctorID, admParams.station,admParams.hospital, reAdmission);
	    indication.setEncounter(new ResourceReferenceDt(adm.getAdmObj().getId()));
	    
	    // filling container class
	    admission.adm = adm.getAdmObj();
	    admission.visit = adm.getVisitObj();
	    admission.patient = readmissionedPatient;
//	    admission.patientID = patId;
	    admission.hospitalization = adm.getHospitalization();
	    admission.condition = indication;
	    
	}

    }
    
    public AdmissionContainer getAdmission(){
	return admission;
    }
    
    
    
    /**
     * With this method you can print out the Json.version of the patioent to the console
     * @param pat
     */
    private void printPat(FhirContext ctx, Patient pat){
	String encoded = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(pat);
	System.out.println(encoded);
    }
    
    
    // useful methods   
    /**
     * method to get a list with the uses, that could be set
     * (may be useful for the GUI (dropdown menu))
     * @return List<String> Use
     */
    public List<String> getListOfUses(boolean print){
	List<String> uses = new ArrayList<String>();
	uses.add("OFFICIAL");
	uses.add("TEMP");
	uses.add("NICKNAME");
	uses.add("ANONYMOUS");
	uses.add("OLD");
	uses.add("MAIDEN");	
	if (print){
	    for(String u:uses){
		System.out.println(u);
	    }
	}	
	return uses;
    }    
    /**
     * method to get a mapping with the uses, that could be set
     * @return List<String> Use
     */
    public List<String> getMapOfUses(boolean print){
	List<String> uses = new ArrayList<String>();
	uses.add("Please use the following mapping to specify the use of the name:");
	uses.add("1: OFFICIAL");
	uses.add("2: TEMP");
	uses.add("3: NICKNAME");
	uses.add("4: ANONYMOUS");
	uses.add("5: OLD");
	uses.add("6: MAIDEN");	
	if (print){
	    for(String u:uses){
		System.out.println(u);
	    }
	}
	return uses;
    }    
    /**
     * method to get a list with gender, that could be set
     * (may be useful for the GUI (dropdown menu))
     * @return List<String> genderList
     */
    public List<String> getListOfGender(boolean print){
	List<String> genderList = new ArrayList<String>();
	genderList.add("F");
	genderList.add("M");
	genderList.add("UN");
	genderList.add("UNK");
	
	if (print){
	    for(String g:genderList){
		System.out.println(g);
	    }
	}
	return genderList;
    }    
    /**
     * method to get a mapping with  gender, that could be set
     * @return List<String> genderList
     */
    public List<String> getMapOfGender(boolean print){
	List<String> genderList = new ArrayList<String>();
	genderList.add("Please use the following mapping to specify the administrative gender of the patient:");
	genderList.add("1: F");
	genderList.add("2: M");
	genderList.add("3: UN");
	genderList.add("4: UNK");
	if (print){
	    for(String g:genderList){
		System.out.println(g);
	    }
	}
	return genderList;
    }    
    /**
     * method to get a list with marital status, that could be set
     * (may be useful for the GUI (dropdown menu))
     * @return List<String> maritalList
     */
    public List<String> getListOfMaritalStatus(boolean print){
	List<String> maritalList = new ArrayList<String>();
	maritalList.add("Unmarried");
	maritalList.add("Annulled");
	maritalList.add("Divorced");
	maritalList.add("Interlocutory");
	maritalList.add("Legally Separated");
	maritalList.add("Married");
	maritalList.add("Polygamous");
	maritalList.add("Never Married");
	maritalList.add("Domestic partner");
	maritalList.add("Widowed");
	maritalList.add("Unknown");
	if (print){
	    for(String m:maritalList){
		System.out.println(m);
	    }
	}
	return maritalList;
    }  
    /**
     * method to get a mapping with marital status, that could be set
     * @return List<String> maritalList
     */
    public List<String> getMapOfMaritalStatus(boolean print){
	List<String> maritalList = new ArrayList<String>();
	maritalList.add("Please use the following mapping to specify the marital status of the patient:");
	maritalList.add("1: Unmarried");
	maritalList.add("2: Annulled");
	maritalList.add("3: Divorced");
	maritalList.add("4: Interlocutory");
	maritalList.add("5: Legally Separated");
	maritalList.add("6: Married");
	maritalList.add("7: Polygamous");
	maritalList.add("8: Never Married");
	maritalList.add("9: Domestic partner");
	maritalList.add("10: Widowed");
	maritalList.add("11: Unknown");
	if (print){
	    for(String m:maritalList){
		System.out.println(m);
	    }
	}	
	return maritalList;
    }
    /**
     * method to get a list with encounter classes, that could be set
     * (may be useful for the GUI (dropdown menu))
     * @return List<String> classList
     */
    public List<String> getListOfAdmClass(boolean print){
	List<String> classList = new ArrayList<String>();
	classList.add("Inpatient");
	classList.add("Outpatient");
	classList.add("Ambulatory");
	classList.add("Emergency");
	classList.add("Home");
	classList.add("Field");
	classList.add("Daytime");
	classList.add("Virtual");
	if (print){
	    for(String m:classList){
		System.out.println(m);
	    }
	}
	return classList;
    }  
    /**
     * method to get a mapping with encounter classes, that could be set
     * @return List<String> classList
     */
    public List<String> getMapOfAdmClass(boolean print){
	List<String> classList = new ArrayList<String>();
	classList.add("Please use the following mapping to specify the class for encounter of the patient:");
	classList.add("1: Inpatient");
	classList.add("2: Outpatient");
	classList.add("3: Ambulatory");
	classList.add("4: Emergency");
	classList.add("5: Home");
	classList.add("6: Field");
	classList.add("7: Daytime");
	classList.add("8: Virtual");
	if (print){
	    for(String m:classList){
		System.out.println(m);
	    }
	}	
	return classList;
    }
    
    private static Patient getPatientFromID(IGenericClient client, IdDt patId)
    {
    	Patient patient = client.read(Patient.class, patId);
    	
    	return patient;
    }
}
