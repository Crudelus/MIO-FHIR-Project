package de.uniluebeck.imi.mio.fhirProject.patientManagement;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.IGenericClient;
    
public class PatientCreation {
   
    private IGenericClient client;
    private List<IdDt> patientIds;

    
    public PatientCreation(FhirContext inContext, IGenericClient inClient)
    {       
        this.client = inClient;     
        
        patientIds = new ArrayList<IdDt>();     
        createDefaultPatients();                
    }

    // Delete all patient entries on our hospital
    public void removeAllPatients()
    {           
        for(IdDt patientID : patientIds)
        {           
            client
            .delete()
            .resourceById(patientID)
            .execute();     
        }           
    }
    
    public List<Patient> getAllPatients()
    {
        List<Patient> patients = new ArrayList<Patient>();
        
        for(IdDt patientID : patientIds)
        {           
            Patient patient = client.read(Patient.class, patientID);
            patients.add(patient);
        }
        
        return patients;
    }

    Patient createPatient(PatientCreationParameters patientParameters)
    {
        // Create new patient wrapper with the input parameters
        MyPatient patient = new MyPatient(patientParameters);       
        
        // Upload the patient, retrieve and store the ID
        IdDt patientID = MyPatient.createPatient(client, patient.getPatientObj());          
        patientIds.add(patientID);

        // Return the patient object    // TODO: The ID is probably not set on the patient yet
        return patient.getPatientObj();
    }   
    
    // Add patient to list for later deletion
    public void addPatientToList(IdDt patientID)
    {
        patientIds.add(patientID);
    }
    
    // Yes, well, now this is not too nice
    private void createDefaultPatients()
    {
        ArrayList<PatientCreationParameters> patientParameters = new ArrayList<PatientCreationParameters>();
        
        // Define default values
        PatientCreationParameters parameters = new PatientCreationParameters();
        parameters.line = "";
        parameters.zip = "23562";
        parameters.city = "Luebeck";
        parameters.maidenName = "";
        
        // First patient
        parameters.firstName = "Balthasar";
        parameters.lastName = "Burghauser";     
        
        parameters.birthDate = new DateTimeDt("1817-04-14");
        parameters.gender = AdministrativeGenderCodesEnum.M;
        parameters.maritalStatus =  MaritalStatusCodesEnum.S;

        patientParameters.add(parameters);
        parameters = new PatientCreationParameters(parameters);
        
        // Second patient
        parameters.firstName = "Arnold";
        parameters.lastName = "Bauer";    
        
        parameters.birthDate = new DateTimeDt("1813-11-23");
        parameters.gender = AdministrativeGenderCodesEnum.M;
        parameters.maritalStatus =  MaritalStatusCodesEnum.S;

        patientParameters.add(parameters);
        parameters = new PatientCreationParameters(parameters);
        
        // Third patient
        parameters.firstName = "Frederike";
        parameters.lastName = "Eyerberg";    
        
        parameters.birthDate = new DateTimeDt("1783-01-16");
        parameters.gender = AdministrativeGenderCodesEnum.F;
        parameters.maritalStatus =  MaritalStatusCodesEnum.W;

        patientParameters.add(parameters);
        parameters = new PatientCreationParameters(parameters);
     
        // Fourth patient
        parameters.firstName = "Achim-Uwe";
        parameters.lastName = "Bergmannsberg";    
        
        parameters.birthDate = new DateTimeDt("1802-06-04");
        parameters.gender = AdministrativeGenderCodesEnum.M;
        parameters.maritalStatus =  MaritalStatusCodesEnum.M;

        patientParameters.add(parameters);
        parameters = new PatientCreationParameters(parameters);
        
        // Fifth patient
        parameters.firstName = "Ursula";
        parameters.lastName = "Steindorff-Wehlmann";    
        
        parameters.birthDate = new DateTimeDt("1752-02-17");
        parameters.gender = AdministrativeGenderCodesEnum.F;
        parameters.maritalStatus =  MaritalStatusCodesEnum.D;

        patientParameters.add(parameters);      
                
        for(PatientCreationParameters currentParameters : patientParameters) createPatient(currentParameters);      
    }

    /**
    * Return the requested patient object from the database or create it if necessary
    */
    Patient providePatient(PatientCreationParameters patParams)
    {
        Patient patient = new Patient();

        // Search existing patient entry
        Bundle results = client
                        .search()
                        .forResource(Patient.class)
                        .where(Patient.GIVEN.matches().value(patParams.firstName))
                        .and(Patient.FAMILY.matches().value(patParams.lastName))
                        .execute();
                    
        if(results.isEmpty())
        {
            // No patient was found, create new entry
            patient = createPatient(patParams);
        }
        else
        {
            // Otherwise get first found ID and return patient
            IdDt patId = results.getEntries().get(0).getId();
            patient = client.read(Patient.class, patId);
        }           

        return patient;
    }
}
