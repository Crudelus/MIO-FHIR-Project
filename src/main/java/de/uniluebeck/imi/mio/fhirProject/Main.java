package de.uniluebeck.imi.mio.fhirProject;

import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
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
import de.uniluebeck.imi.mio.fhirProject.devices.MIODeviceSystem;
import de.uniluebeck.imi.mio.fhirProject.patientManagement.AdmissionContainer;
import de.uniluebeck.imi.mio.fhirProject.patientManagement.AdmissionParameters;
import de.uniluebeck.imi.mio.fhirProject.patientManagement.IPatientManagementSystem;
import de.uniluebeck.imi.mio.fhirProject.patientManagement.PatientCreationParameters;
import de.uniluebeck.imi.mio.fhirProject.patientManagement.PatientManagementSystem;

public class Main
{
	public static void main(String[] args)
	{
		//runTest();
		
		runScenario();
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
	    PatientCreationParameters scenarioPatientParameters = null;// < Fill with GUI or terminal input> 
	    AdmissionParameters admissionParameters = null; // < Fill with GUI or terminal input> 
	    AdmissionContainer scenarioAdmission = patientManagement.planAdmission(scenarioPatientParameters, admissionParameters);
	    
	    // Admit patient for first treatment
		scenarioAdmission = patientManagement.admitPatient(scenarioAdmission, 
															admissionParameters, 
															new ResourceReferenceDt(birthStation), 
															101);	

	    Patient scenarioPatient = scenarioAdmission.patient;
	    

	    // Group 3: Create observation for patient, get nurse
	    // encounter is known from admission result 
	    List<Practitioner> practitioners = patientManagement.getNurseList();
	    // < Display on GUI for selection> 

	    // Create child
	    PatientCreationParameters childPatientParameters = null; // < Fill with GUI or terminal input> 
	    AdmissionParameters childAdmissionParameters = null; // < Fill with GUI or terminal input> 
	    
	    Organization nicuStation = patientManagement.getNICU();
	    
	    AdmissionContainer childAdmission = patientManagement.planAdmission(childPatientParameters, childAdmissionParameters);	    
	    childAdmission = patientManagement.admitPatient(childAdmission, 
														childAdmissionParameters, 
														new ResourceReferenceDt(nicuStation), 
														101);	

	    // Transfer scenarioPatient
	    ResourceReferenceDt imcReference = new ResourceReferenceDt(patientManagement.getIMC());
	    long durationIMC = 0; // < Fill with GUI or terminal input>
	    String diagnosisICD = null; // < Fill with GUI or terminal input>
	    String diagnosisDescription = null; // < Fill with GUI or terminal input>

	    patientManagement.transferPatient(scenarioAdmission, imcReference, durationIMC, diagnosisICD, diagnosisDescription);
	    
	    
	    // Discharge both, use admission as input so that encounter can be used directly
	    patientManagement.dischargePatient(scenarioAdmission);
	    patientManagement.dischargePatient(childAdmission);

	    // Divorce of patient
	    scenarioPatient.setMaritalStatus(MaritalStatusCodesEnum.D);
	    patientManagement.updatePatient(scenarioPatient);

	    // Readmission
	    AdmissionParameters secondAdmissionParameters = null; // < Fill with GUI or terminal input> 
	    
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
	}  
}
