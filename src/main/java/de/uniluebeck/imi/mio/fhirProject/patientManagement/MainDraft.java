package de.uniluebeck.imi.mio.fhirProject.patientManagement;

import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.rest.client.IGenericClient;

public class MainDraft
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
    PatientCreationParameters scenarioPatientParameters;// < Fill with GUI or terminal input> 
    AdmissionParameters admissionParameters; // < Fill with GUI or terminal input> 
    Admission scenarioAdmission = patientManagement.admitPatient(scenarioPatientParameters, admissionParameters);	

    Patient scenarioPatient = scenarioAdmission.patient;

    // Group 1: Create observation for patient, get nurse [Maybe reference for practitioner would suffice?]
    // encounter is known from admission result 
    List<Practitioner> practitioners = patientManagement.getNurses();
        
    // Create child
    PatientCreationParameters childPatientParameters; // < Fill with GUI or terminal input> 
    AdmissionParameters childAdmissionParameters; // < Fill with GUI or terminal input> 
    Admission childAdmission = patientManagement.admitPatient(childPatientParameters, childAdmissionParameters);	
    
    // Work-in-progress:
    /*
    // Transfer scenarioPatient
    Organization imc = patientManagement.getIMC();
    String durationIMC = new String(); // < Fill with GUI or terminal input> 
        
    patientManagement.transferPatient(scenarioPatient, imc, durationIMC);
    
    // Discharge both, use admission as input so that encounter can be used directly
    patientManagement.dischargePatient(scenarioAdmission);
    patientManagement.dischargePatient(childAdmission);

    // Divorce of patient
    scenarioPatient.setMaritalStatus(MaritalStatusCodesEnum.S);
    patientManagement.updatePatient(scenarioPatient);

    // Readmission
    AdmissionParameters secondAdmissionParameters; // < Fill with GUI or terminal input> 
    Admission secondAdmission = patientManagement.admitPatient(scenarioPatientParameters, secondAdmissionParameters);	

    // Get previous encounters
    List<Encounter> getAllPatientEncounters(scenarioPatient);    
	
    // At end of scenario:
    // Clean infrastructure from server
    patientManagement.clearEntries();
    */
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
