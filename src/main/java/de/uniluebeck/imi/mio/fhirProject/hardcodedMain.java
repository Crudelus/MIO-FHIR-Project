package de.uniluebeck.imi.mio.fhirProject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Condition;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterClassEnum;
import ca.uhn.fhir.model.dstu.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import de.uniluebeck.imi.mio.fhirProject.devices.MIODeviceSystem;
import de.uniluebeck.imi.mio.fhirProject.patientManagement.AdmissionContainer;
import de.uniluebeck.imi.mio.fhirProject.patientManagement.AdmissionParameters;
import de.uniluebeck.imi.mio.fhirProject.patientManagement.IPatientManagementSystem;
import de.uniluebeck.imi.mio.fhirProject.patientManagement.PatientCreationParameters;
import de.uniluebeck.imi.mio.fhirProject.patientManagement.PatientManagementSystem;

public class hardcodedMain
{
	public static void main(String[] args)
	{
		//runTest();
		
		runScenario();
		
//		resetServer();
	}    
	
	/**
	 * Try parts of the functionality with hardcoded parameters
	 */
	public static void runTest()
	{
		String serverBase = "http://fhirtest.uhn.ca/base";

	    // Server infrastructure:
	    FhirContext context = new FhirContext();
	    IGenericClient client = context.newRestfulGenericClient(serverBase);

	    // Create and initialize 
	    IPatientManagementSystem patientManagement = new PatientManagementSystem(client);
	    
	    // Create test patient parameters
	    PatientCreationParameters patParams = createTestPatientParameters();
	    
	    // Create test admission parameters
	    Practitioner testDoctor = patientManagement.getDoctorList().get(0);
	    Organization testStation = patientManagement.getBirthStation();
	    IdDt testHospitalId = patientManagement.getHospitalID();
	    AdmissionParameters admissionParams = createTestAdmissionParameters(testDoctor.getId(), 
	    																	testStation.getId(), 
																			testHospitalId);

	    // Execute test admission, creating a planned visit encounter
	    AdmissionContainer admission = patientManagement.planAdmission(patParams, admissionParams);
	    
	    // Execute test admission admission, 
	    // setting the visit-encounter to 'in progress'
	    // and creating an adm sub-encounter
	    admissionParams.admissionClass = EncounterClassEnum.EMERGENCY;
	    ResourceReferenceDt targetOrganizationReference = new ResourceReferenceDt(testStation);
	    patientManagement.admitPatient(admission, admissionParams, targetOrganizationReference, 0);
	
	    patientManagement.transferPatient(admission, 
	    		new ResourceReferenceDt(patientManagement.getIMC()), 
	    		42, 
	    		"080", 
	    		"Evil twin birth");	    	    
	    
	    admission.patient.setMaritalStatus(MaritalStatusCodesEnum.D);
	    patientManagement.updatePatient(admission.patient);
	    
	    patientManagement.dischargePatient(admission);
	    
	    patientManagement.clearEntries();
	}
	
	private static PatientCreationParameters createTestPatientParameters()
	{
		PatientCreationParameters parameters = new PatientCreationParameters();
		
		parameters.firstName = "Siegfried-Ephraim"; 
		parameters.lastName = "Brauzelheimsmann"; 
		parameters.maidenName = "Brauzelheimsfrau"; 
		parameters.birthDate = new DateTimeDt("1952-02-12"); 
		parameters.gender = AdministrativeGenderCodesEnum.M;
		parameters.maritalStatus = MaritalStatusCodesEnum.S;
		
		// Address:
		parameters.line = "123";
		parameters.zip = "456";
		parameters.city = "789";
		
        return parameters;
	}

	private static AdmissionParameters createTestAdmissionParameters(IdDt doctorId, IdDt stationId, IdDt hospitalId)
    {
        AdmissionParameters parameters = new AdmissionParameters();

        parameters.admissionClass = EncounterClassEnum.AMBULATORY;
        parameters.diagnosisICD = "080"; 	// "O80"
        parameters.diagnosisDescription = "Entbindung-Spontangeburt";
        parameters.doctorID = doctorId;
        parameters.station = stationId;    	
    	
    	return parameters;    
    }
	
	public static void runScenario()
	{
		String serverBase = "http://fhirtest.uhn.ca/base";

	    // Server infrastructure:
	    FhirContext context = new FhirContext();
	    IGenericClient client = context.newRestfulGenericClient(serverBase);

	    
	    // Create and initialize 
	    IPatientManagementSystem patientManagement = new PatientManagementSystem(client);

	    //MIODeviceSystem initialize
	    MIODeviceSystem deviceManager = new MIODeviceSystem(serverBase, context, patientManagement);
	    
	    // Scenario:   
	    // Get parameters for patient admission
	    Organization birthStation = patientManagement.getBirthStation();
	            
	    // Plan stay for patient
	    PatientCreationParameters scenarioPatientParameters = new PatientCreationParameters();// < Fill with GUI or terminal input> 
	    
	    scenarioPatientParameters.firstName = "Tony";
	    scenarioPatientParameters.lastName = "Gruenlich";
	    scenarioPatientParameters.birthDate = new DateTimeDt("2015-03-03");
	    scenarioPatientParameters.city = "Luebeck";
	    scenarioPatientParameters.gender = AdministrativeGenderCodesEnum.F;
	    scenarioPatientParameters.maidenName = "Buddenbrook";
	    scenarioPatientParameters.maritalStatus = MaritalStatusCodesEnum.M;
	    scenarioPatientParameters.zip = "23562";
	    scenarioPatientParameters.line = "Hauptstrasse 456";
	    
	    AdmissionParameters admissionParameters = new AdmissionParameters(); // < Fill with GUI or terminal input> 
	    
	    admissionParameters.admissionClass = EncounterClassEnum.INPATIENT;
	    admissionParameters.diagnosisICD = "080";
	    admissionParameters.diagnosisDescription = "Entbindung";
	    
	    AdmissionContainer scenarioAdmission = patientManagement.planAdmission(scenarioPatientParameters, admissionParameters);
	    
	    // Admit patient for first treatment
		scenarioAdmission = patientManagement.admitPatient(scenarioAdmission, 
															admissionParameters, 
															new ResourceReferenceDt(birthStation), 
															101);	

	    Patient scenarioPatient = scenarioAdmission.patient;
	    

	    // Group 1: Create observation for patient, get nurse
	    // encounter is known from admission result 
	    List<Practitioner> practitioners = patientManagement.getNurseList();
	    // < Display on GUI for selection> 

	    // Create child
	    PatientCreationParameters childPatientParameters = new PatientCreationParameters(); // < Fill with GUI or terminal input> 
	    
	    childPatientParameters.firstName = "Erika";
	    childPatientParameters.lastName = "Gruenlich";
	    childPatientParameters.city = "Luebeck";
	    childPatientParameters.gender = AdministrativeGenderCodesEnum.F;
	    childPatientParameters.birthDate = new DateTimeDt("2015-03-03");
	    childPatientParameters.line = "Hauptstrasse 457";
	    childPatientParameters.maidenName = "Gruenlich";
	    childPatientParameters.maritalStatus = MaritalStatusCodesEnum.S;
	    childPatientParameters.zip = "23562";
	    
	    AdmissionParameters childAdmissionParameters = new AdmissionParameters(); // < Fill with GUI or terminal input> 
	    
	    admissionParameters.admissionClass = EncounterClassEnum.INPATIENT;
	    admissionParameters.diagnosisICD = "089";
	    admissionParameters.diagnosisDescription = "Wurde Entbindung";	    
	    
	    Organization nicuStation = patientManagement.getNICU();
	    
	    AdmissionContainer childAdmission = patientManagement.planAdmission(childPatientParameters, childAdmissionParameters);	    
	    childAdmission = patientManagement.admitPatient(childAdmission, 
														childAdmissionParameters, 
														new ResourceReferenceDt(nicuStation), 
														101);	
	    
	  

	    // Transfer scenarioPatient
	    ResourceReferenceDt imcReference = new ResourceReferenceDt(patientManagement.getIMC());
	    long durationIMC = 0; // < Fill with GUI or terminal input>
	    String diagnosisICD = "WTF"; // < Fill with GUI or terminal input>
	    String diagnosisDescription = "Atomnot"; // < Fill with GUI or terminal input>

	    patientManagement.transferPatient(scenarioAdmission, imcReference, durationIMC, diagnosisICD, diagnosisDescription);
	    
	    
	    // Discharge both, use admission as input so that encounter can be used directly
	    patientManagement.dischargePatient(childAdmission);
	    patientManagement.dischargePatient(scenarioAdmission);

	    // Divorce of patient
	    scenarioPatient.setMaritalStatus(MaritalStatusCodesEnum.D);
	    patientManagement.updatePatient(scenarioPatient);

	    // Readmission
	    AdmissionParameters secondAdmissionParameters = new AdmissionParameters(); // < Fill with GUI or terminal input> 
	    secondAdmissionParameters.admissionClass = EncounterClassEnum.INPATIENT;
	    secondAdmissionParameters.diagnosisICD = "000";
	    secondAdmissionParameters.diagnosisDescription = "Nicht Entbindung";
	    
	    AdmissionContainer secondAdmission = patientManagement.planAdmission(childPatientParameters, childAdmissionParameters);
	    secondAdmission = patientManagement.admitPatient(secondAdmission, 
														secondAdmissionParameters, 
														new ResourceReferenceDt(nicuStation), 
														101);

	    // Get previous encounters
	    List<Encounter> encounterList = patientManagement.getAllPatientEncounters(scenarioPatient);    
	    // < Display on GUI for selection>
	    
	    // At end of scenario:
	    // Clean infrastructure from server
	    patientManagement.clearEntries();
	    deviceManager.delAll();	  
	    System.out.println("#########################     FERTIG!    #########################");
	    
	    
	    
	    

	}  
	
	
	public static void deleteOrganization(IGenericClient client, String organizationName) { // Find resource by name and return a bundle containing the resource-information 
		Bundle response = client
				.search()
				.forResource(Organization.class)
				.where(Organization.NAME.matches().value(organizationName))
				.execute(); 
		
		// If the bundle isn't empty, delete the resource on the server using the resource-ID 
		if(!response.isEmpty()) 
			{ 
			for(int i= 0; i < response.size(); i++) 
				{ IdDt id = response.getEntries().get(i).getId(); 
				client .delete().resourceById(id).execute(); 
				} 
			} 
		}
	
	
	public static void deletePatient(IGenericClient client, String patientName) { // Find resource by name and return a bundle containing the resource-information 
		Bundle response = client
				.search()
				.forResource(Patient.class)
				.where(Patient.NAME.matches().value(patientName))
				.execute(); 
		
		// If the bundle isn't empty, delete the resource on the server using the resource-ID 
		if(!response.isEmpty()) 
			{ 
			for(int i= 0; i < response.size(); i++) 
				{ IdDt id = response.getEntries().get(i).getId(); 
				client .delete().resourceById(id).execute(); 
				} 
			} 
		}
	
	
	
	
	public static void deleteEncounter(IGenericClient client) { // Find resource by name and return a bundle containing the resource-information 
		Bundle response = client.search().forResource(Encounter.class) 
				.where(new StringClientParam("subject.name").matches().value("gruenlich")) 
				.execute();
		
		// If the bundle isn't empty, delete the resource on the server using the resource-ID 
		if(!response.isEmpty()) 
			{ 
			for(int i= 0; i < response.size(); i++) 
				{ IdDt id = response.getEntries().get(i).getId(); 
				client .delete().resourceById(id).execute(); 
				} 
			} 
		}
	
	
	
	
	public static void deleteCondition(IGenericClient client) { // Find resource by name and return a bundle containing the resource-information 
		Bundle response = client.search().forResource(Condition.class) 
				.where(new StringClientParam("subject.name").matches().value("gruenlich")) 
				.execute();
		
		// If the bundle isn't empty, delete the resource on the server using the resource-ID 
		if(!response.isEmpty()) 
			{ 
			for(int i= 0; i < response.size(); i++) 
				{ IdDt id = response.getEntries().get(i).getId(); 
				client .delete().resourceById(id).execute(); 
				} 
			} 
		}
	
	
	public static void resetServer()
	{
		String serverBase = "http://fhirtest.uhn.ca/base";

	    // Server infrastructure:
	    FhirContext context = new FhirContext();
	    IGenericClient client = context.newRestfulGenericClient(serverBase);
	    
		deletePatient(client, "gruenlich");
		deletePatient(client, "buddenbrook");
		deleteEncounter(client);
		deleteOrganization(client, "MIO Krankenhaus Hamburg");
		deleteCondition(client);
	}
}
