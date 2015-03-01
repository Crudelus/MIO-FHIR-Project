package de.uniluebeck.imi.mio.fhirProject.patientManagement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.DurationDt;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Condition;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Encounter.Hospitalization;
import ca.uhn.fhir.model.dstu.valueset.EncounterReasonCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterStateEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;

/**
 * Class of the main Admission:
 * putting Patient, Indication and admission together
 * gets input for creating a patient, the class of admission, the admission diagnosis (ICD+description), admitting doctor
 * @author Anne
 *
 */
public class AdmissionSystem {
        
	private IGenericClient client;  
    private List<IdDt> encounterIds;
	
	/**
	 *	
	 * @param inClient
	 */
    public AdmissionSystem(IGenericClient inClient)
    {
    	this.client = inClient;
    	
    	encounterIds = new ArrayList<IdDt>();
    }
    
    /**
     *  Fill admission container with planned visit encounter.
     *  No adm encounter is created yet.
     * @param patParams
     * @param admParams
     * @param hospitalID
     * @param patientCreation
     * @return
     */
    public AdmissionContainer createPlannedVisitEncounter(PatientCreationParameters patParams,
											AdmissionParameters admParams, 
											IdDt hospitalId,
											PatientCreation patientCreation)
    {
    	// Due to mutual dependencies some resources must first be created 
    	// locally before uploading:
    	
	    // Get or create patient
	    Patient patient = patientCreation.providePatient(patParams);
	
		// Create and upload indication
		Condition condition = new MyCondition(client, patient, admParams).getCondObj();
		   	
		// Create hospitalization
		Hospitalization hosp = createLocalHospitalization(admParams);
		
		// Create visit encounter	
		Encounter visit = createLocalVisitEncounter(admParams, patient, hospitalId);
		visit.setIndication(new ResourceReferenceDt(condition.getId()));
		visit.setHospitalization(hosp);
		
	    // Upload visit encounter    
		IdDt visitId = createEncounter(client, visit);		
		
		// Update Condition with visit encounter id    
	    condition.setEncounter(new ResourceReferenceDt(visitId));	    
	    MyCondition.updateCondition(client, condition);
	    
	    // Fill and return admission container
    	AdmissionContainer admissionResult = new AdmissionContainer();
    	 
	    admissionResult.visit = visit;
	    admissionResult.patient = patient;
	    admissionResult.hospitalization = visit.getHospitalization();
	    admissionResult.condition = condition;		
	    
	    return admissionResult;	    
    }
    
    /**
     *  Create a hospitalization without uploading it
     * @param admParams
     * @return
     */
    private Hospitalization createLocalHospitalization(AdmissionParameters admParams)
    {
		Hospitalization hosp = new Hospitalization();
		  
		hosp.addAccomodation().setBed(new ResourceReferenceDt(admParams.station));
		hosp.setReAdmission(false); // accepts boolean and sets it true, if the patient was admitted one more time
	    
		return hosp;
    }
    
    /**
     * Create a visit encounter without uploading it.
     * The visit encounter is the 'big' encounter with many adm sub-encounters
     * for the individual treatments.
     * @return
     */
    private Encounter createLocalVisitEncounter(AdmissionParameters admParams, 
    											Patient patient,
    											IdDt hospitalId)
    {		    
		Encounter visit = new Encounter();
		
		// big encounter to record the whole visit of the patient
		visit.addIdentifier("http://kh-hh.de/mio/encounters","Visit-"+(int)(Math.random()*1000));
		visit.setStatus(EncounterStateEnum.PLANNED);
		visit.setClassElement(admParams.admissionClass);
		
		visit.setSubject(new ResourceReferenceDt(patient.getId()));
		visit.addParticipant().setIndividual(new ResourceReferenceDt(admParams.doctorID));	
		visit.setServiceProvider(new ResourceReferenceDt(hospitalId));

		Date date = java.util.Calendar.getInstance().getTime();
	    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	    String dateString = dateFormatter.format(date);
		
		visit.setPeriod(new PeriodDt().setStart(new DateTimeDt(dateString)));		
		
		
		return visit;
    }
    
    /**
     * Create the first adm encounter for a planned visit encounter.
     * The visit encounter is set from 'planned' to 'in-progress'.
     * Any further admissions should be added by transferring
     * @param admission
     * @param admParams
     * @param duration
     * @param targetOrganizationReference
     */
    public void addAdmissionEncounter(AdmissionContainer admission,
    								AdmissionParameters admParams,
    								long duration,
    								ResourceReferenceDt targetOrganizationReference)
    {
    	// Mark visit encounter as in progress and update on server
    	admission.visit.setStatus(EncounterStateEnum.IN_PROGRESS);
    	updateEncounter(client, admission.visit);
    	
    	// Create new duration
		DurationDt encounterDuration = new DurationDt();
		encounterDuration.setValue(duration);
		
		// Create new adm encounter
		Encounter beginningEncounter = new Encounter();
		beginningEncounter.setStatus(EncounterStateEnum.IN_PROGRESS);
		beginningEncounter.setClassElement(admParams.admissionClass);
		beginningEncounter.setLength(encounterDuration);

		//beginningEncounter.setReason(EncounterReasonCodesEnum.valueOf(admParams.diagnosisICD));
		beginningEncounter.setSubject(admission.visit.getSubject());

		ResourceReferenceDt visitReference = new ResourceReferenceDt(admission.visit);
		beginningEncounter.setPartOf(visitReference);
	
		// Create new hospitalization
		Hospitalization beginningHospitalization = new Hospitalization();
		beginningHospitalization.setDestination(targetOrganizationReference);		
		beginningEncounter.setHospitalization(beginningHospitalization);
		
		// Create new encounter on server		
		createEncounter(client, beginningEncounter);		
		
		admission.adm = beginningEncounter;
    }
    
    
    
    /**
     * Call the upload to the server and store the resulting id
     * for later deletion
     * @param client
     * @param encounter
     * @return
     */
    public IdDt createEncounter(IGenericClient client, Encounter encounter)
    {
    	IdDt encounterId = uploadEncounter(client, encounter);
    	
    	encounterIds.add(encounterId);
    	
    	return encounterId;
    }
    
    /**
     * Upload an encounter to the server, returning the id
     * @param client
     * @param encounter
     * @return
     */
    private IdDt uploadEncounter(IGenericClient client, Encounter encounter)
    {
		MethodOutcome  outcome = client
				.create()
				.resource(encounter)
				.prettyPrint()
				.encodedXml()
				.execute();
		
		IdDt id = outcome.getId();        
        String elementSpecificId = id.getBaseUrl();
        String idPart = id.getIdPart();
        IdDt idNonVersioned = new IdDt(elementSpecificId+"/"+id.getResourceType()+"/"+idPart);

        // Set ID on local encounter object
        encounter.setId(idNonVersioned);         
        
        return idNonVersioned;
    }   
    
    /**
     * Update an encounter on the server
     * @param client
     * @param encounter
     * @return
     */
    public boolean updateEncounter(IGenericClient client, Encounter encounter)
    {
    	try {
			client
			.update()
			.resource(encounter)
			.execute();
			return true;
		} catch (Exception e) {
			System.err.println("Encounter update failed");
			e.printStackTrace();
			return false;
		}    	
    }
 
    /**
     * Remove all encounters known by id from the server
     */
    public void removeAllEncounters()
    {
    	for(IdDt encounterID : encounterIds)
        {           
            client
            .delete()
            .resourceById(encounterID)
            .execute();     
        }   
    }
}
