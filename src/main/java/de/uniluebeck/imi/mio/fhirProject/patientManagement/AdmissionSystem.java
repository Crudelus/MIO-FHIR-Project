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
	private FhirContext ctx;
    
    private List<IdDt> encounterIds;

	
    /**
     * Constructor of the main admission
     * @param client
     * @param ctx
     * @param patParams
     * @param admParams
     * @param hospitalID
     * @param patientCreation
     */
    
    public AdmissionSystem(FhirContext inCtx, IGenericClient inClient)
    {	    	
    	this.ctx = inCtx;
    	this.client = inClient;
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
											IdDt hospitalID, // TODO: Use this one
											PatientCreation patientCreation)
    {
    	AdmissionContainer admission = new AdmissionContainer();
    	
	    // GET OR CREATE PATIENT
	    Patient patient = patientCreation.providePatient(patParams);
	
		// CREATE INDICATION
		Condition condition = new MyCondition(client, patient, admParams).getCondObj();
		   	
		// CREATE VISIT ENCOUNTER	
		Date date = java.util.Calendar.getInstance().getTime();
	    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	    String dateString = dateFormatter.format(date);
		    
		Encounter visit = new Encounter();
		// big encounter to record the whole visit of the patient
		visit.addIdentifier("http://kh-hh.de/mio/encounters","Visit-"+(int)(Math.random()*1000));
		visit.setStatus(EncounterStateEnum.PLANNED);
		visit.setClassElement(admParams.admissionClass);
		visit.setSubject(new ResourceReferenceDt(patient.getId()));
		visit.addParticipant().setIndividual(new ResourceReferenceDt(admParams.doctorID));	
		visit.setPeriod(new PeriodDt().setStart(new DateTimeDt(dateString)));
		visit.setIndication(new ResourceReferenceDt(condition.getId()));
		
		// CREATE HOSPITALIZATION
		Hospitalization hosp = new Hospitalization();
		  
		hosp.addAccomodation().setBed(new ResourceReferenceDt(admParams.station));
		hosp.setReAdmission(false); // accepts boolean and sets it true, if the patient was admitted one more time
	    
		visit.setHospitalization(hosp);
		
	    // Upload visit    
		IdDt visitId = createEncounter(client, visit);		
		
	    condition.setEncounter(new ResourceReferenceDt(visit.getId()));
	
	    // Update Condition with visit encounter id    
	    MyCondition.updateCondition(client, condition);
	    
	    // filling container class
	    //admission.adm = adm.getAdmObj();
	    admission.visit = visit;
	    admission.patient = patient;
	    admission.hospitalization = visit.getHospitalization();
	    admission.condition = condition;
		
	    
	    /* Formerly for already existing patients
	    System.out.println("###### 3.2. Ja. Die Patienten-ID wird abgerufen...");
	    boolean reAdmission = true;
	    IdDt patId = results.getEntries().get(0).getId();
	    Patient readmissionedPatient = getPatientFromID(client, patId);
	    System.out.println("PatID: "+patId);
	
	    Condition indication = new MyCondition(client, readmissionedPatient, admParams.doctorID, admParams.diagnosisICD, admParams.diagnosisDescription).getCondObj();
	    System.out.println("###### admission loaded");
	    MyAdmission adm = new MyAdmission(client, readmissionedPatient, admParams.admissionClass, indication.getId(),admParams.doctorID, admParams.station, hospitalID, reAdmission);
	    indication.setEncounter(new ResourceReferenceDt(adm.getAdmObj().getId()));
	    MyCondition.updateCondition(client, indication);
	    
	    // filling container class
	    admission.adm = adm.getAdmObj();
	    admission.visit = adm.getVisitObj();
	    admission.patient = readmissionedPatient;
	//	admission.patientID = patId;
	    admission.hospitalization = adm.getHospitalization();
	    admission.condition = indication;
	    */
		
	    return admission;	    
    }
    
    
    /**
     * 	Add first adm encounter to input admission container 
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

		beginningEncounter.setReason(EncounterReasonCodesEnum.valueOf(admParams.diagnosisICD));
		beginningEncounter.setSubject(admission.visit.getSubject());

		ResourceReferenceDt visitReference = new ResourceReferenceDt(admission.visit);
		beginningEncounter.setPartOf(visitReference);
	
		// Create new hospitalization
		Hospitalization beginningHospitalization = new Hospitalization();
		beginningHospitalization.setDestination(targetOrganizationReference);		
		beginningEncounter.setHospitalization(beginningHospitalization);
		
		// Create new encounter on server		
		IdDt admId = createEncounter(client, beginningEncounter);
		
		
		admission.adm = beginningEncounter;
    }
    
    public IdDt createEncounter(IGenericClient client, Encounter encounter)
    {
    	IdDt encounterId = uploadEncounter(client, encounter);
    	
    	encounterIds.add(encounterId);
    	
    	return encounterId;
    }
    
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
