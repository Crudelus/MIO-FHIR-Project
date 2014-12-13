package patientManagement;

import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.model.dstu.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.IGenericClient;

public class Main
{
	public static void main(String[] args)
	{
		String serverBase = "http://fhirtest.uhn.ca/base";

	    // Server infrastructure:
	    FhirContext context = new FhirContext();
	    IGenericClient client = context.newRestfulGenericClient(serverBase);

	    // Create and initialize 
	    IPatientManagementSystem patientManagement = new PatientManagementSystem(context, client);
	    
	    // Scenario:   
	    // Get parameters for patient admission
	    Organization birthStation = patientManagement.getBirthStation();
	            
	    // Admit patient (creates encounter, hospitalization, composition etc)
	    PatientCreationParameters scenarioPatientParameters = null;// < Fill with GUI or terminal input> 
	    AdmissionParameters admissionParameters = null; // < Fill with GUI or terminal input> 
	    AdmissionContainer scenarioAdmission = patientManagement.planAdmission(scenarioPatientParameters, admissionParameters);
	    
	    // TODO: Currently being overhauled
		//AdmissionContainer scenarioAdmission = patientManagement.admitPatient(scenarioPatientParameters, admissionParameters);	

	    Patient scenarioPatient = scenarioAdmission.patient;
	    

	    // Group 1: Create observation for patient, get nurse [Maybe reference for practitioner would suffice?]
	    // encounter is known from admission result 
	    List<Practitioner> practitioners = patientManagement.getNurses();
	        
	    // Create child
	    PatientCreationParameters childPatientParameters = null; // < Fill with GUI or terminal input> 
	    AdmissionParameters childAdmissionParameters = null; // < Fill with GUI or terminal input> 
	    
	    // TODO: Currently being overhauled		
	    //AdmissionContainer childAdmission = patientManagement.admitPatient(childPatientParameters, childAdmissionParameters);	
	    
	    // 	    
	    
	    // Transfer scenarioPatient
	    ResourceReferenceDt imcReference = new ResourceReferenceDt(patientManagement.getIMC());
	    long durationIMC = 0; // < Fill with GUI or terminal input>
	    String diagnosisICD = null; // < Fill with GUI or terminal input>
	    String diagnosisDescription = null; // < Fill with GUI or terminal input>

	    patientManagement.transferPatient(scenarioAdmission, imcReference, durationIMC, diagnosisICD, diagnosisDescription);
	    
	    
	    // Discharge both, use admission as input so that encounter can be used directly
	    patientManagement.dischargePatient(scenarioAdmission);
	    //patientManagement.dischargePatient(childAdmission);

	    // Divorce of patient
	    scenarioPatient.setMaritalStatus(MaritalStatusCodesEnum.D);
	    patientManagement.updatePatient(scenarioPatient);

	    // Readmission
	    AdmissionParameters secondAdmissionParameters = null; // < Fill with GUI or terminal input> 
	    
	    // TODO: Currently being overhauled			    
	    //AdmissionContainer secondAdmission = patientManagement.admitPatient(scenarioPatientParameters, secondAdmissionParameters);	

	    // Get previous encounters
	    //List<Encounter> encounterList = patientManagement.getAllPatientEncounters(scenarioPatient);    
		
	    // At end of scenario:
	    // Clean infrastructure from server
	    patientManagement.clearEntries();
	  
	}
    
}

/*

// Collection of input information for the creation of an Encounter, Hospitalization and Diagnostic Order
class AdmissionParameters
{
    public String reason; // ICD 10
    Organization station;

    //<diagnostic order parameters> + Composition
}


// Unless other groups need actual objects, references would suffice
class Admission
{
    Encounter encounter;
    Hospitalization hospitalization;
    DiagnosticOrder diagnosticOrder;
	Composition composition;
}
*/
