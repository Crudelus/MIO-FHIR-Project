package de.uniluebeck.imi.mio.fhirProject.patientManagement;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.DurationDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.model.dstu.resource.Encounter.Hospitalization;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.CompositionStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterReasonCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterStateEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.model.dstu.resource.Composition;

public class PatientManagementSystem implements IPatientManagementSystem{

	private IGenericClient client;
	
	private InfrastructureCreation infrastructure;
	private PatientCreation patientCreation;
	private DoctorCreation doctorCreation;
	private NurseCreation nurseCreation;
	
	private AdmissionSystem admissionSystem;
	
	private IdDt hospitalID;
	
	private List<IdDt> compositionIds;
		
	
	public PatientManagementSystem(IGenericClient inClient)
	{
		this.client = inClient;
		compositionIds = new ArrayList<IdDt>();
		
		//Create default infrastructure
		infrastructure = new InfrastructureCreation(inClient);
		
		// Get hospitalID from infrastructure
		hospitalID = infrastructure.getHospitalID();
		
		// Create default patients
		patientCreation = new PatientCreation(client);
          
		// Create doctors
		doctorCreation = new DoctorCreation(client);
		
		// Create nurses
		nurseCreation = new NurseCreation(client);
	
		// Create admission system
		admissionSystem = new AdmissionSystem(client);		
	}
	
    /**
     * Create admission container with planned visit encounter using the input parameters
     */
	@Override
	public AdmissionContainer planAdmission(PatientCreationParameters patientParameters, 
								 			AdmissionParameters admissionParameters) 
	{	
		AdmissionContainer admission = admissionSystem.createPlannedVisitEncounter(patientParameters, admissionParameters, hospitalID, patientCreation);
			
		return admission;
	}
	
	/**
	 * Add adm encounter to input admission container using the input parameters
	 */
	@Override
	public AdmissionContainer admitPatient(AdmissionContainer admission,							
							AdmissionParameters admParams,
							ResourceReferenceDt targetOrganizationReference,
							long duration) 
	{    
		admissionSystem.addAdmissionEncounter(admission, admParams, duration, targetOrganizationReference);
	
		return admission;
	}
	
	@Override
	public boolean dischargePatient(AdmissionContainer admission) {
		
		// Mark visit encounter as finished
		admission.visit.setStatus(EncounterStateEnum.FINISHED);
		boolean updateSucess = admissionSystem.updateEncounter(client, admission.visit);				
		
		Encounter dischargeEncounter = new Encounter();
		
		executePatientTransfer(admission, 
								null, 
								dischargeEncounter, 
								admission.hospitalization, 
								true);

		return updateSucess;
	}
	
	/**
	 * This method is used to transfer patients from one station to another. The running encounter-data is reused as much 
	 * as possible and it's status is then set to "finished". A new encounter is created.
	 * If the input-encounter is the main encounter, it is set as part of the beginning encounter.
	 * @param client
	 * @param runningEncounter
	 * @param targetOrganizationRefernce
	 * @param duration
	 */
	@Override
	public boolean transferPatient(AdmissionContainer admission, 
			ResourceReferenceDt targetOrganizationReference,
			long duration,
			String diagnosisICD,
			String diagnosisDescription) 
	{
		// Fill transfer container
		TransferContainer transferContainer = new TransferContainer();
		transferContainer.targetOrganizationReference = targetOrganizationReference;
		transferContainer.duration = duration;
		transferContainer.diagnosisICD = diagnosisICD;
		transferContainer.diagnosisDescription = diagnosisDescription;		
		
		// Create new duration
		DurationDt encounterDuration = new DurationDt();
		encounterDuration.setValue(transferContainer.duration);
		
		// Check whether admission encounter is main encounter or if a sub-encounter already exists
		Encounter runningEncounter = admission.adm;
		
		// Create new hospitalization
		Hospitalization beginningHospitalization = new Hospitalization();
		beginningHospitalization.setOrigin(runningEncounter.getHospitalization().getOrigin());
		
		// Fill new encounter with values of the ending encounter that are still valid
		Encounter beginningEncounter = new Encounter();
		beginningEncounter.setStatus(EncounterStateEnum.IN_PROGRESS);
		beginningEncounter.setClassElement(runningEncounter.getClassElement());
		beginningEncounter.setLength(encounterDuration);

		//beginningEncounter.setReason(EncounterReasonCodesEnum.valueOf(transferContainer.diagnosisICD));
		
		//TODO: add narrative (diagnosisDescription) to encounter reason	
						
		return executePatientTransfer(admission, 
									targetOrganizationReference,
									beginningEncounter, 
									beginningHospitalization,
									false);
	}

	
	private boolean executePatientTransfer(AdmissionContainer admission, 
										ResourceReferenceDt targetOrganizationReference,
										Encounter beginningEncounter,
										Hospitalization beginningHospitalization,
										boolean discharge)
	{
		// Update old encounter on server, mark as finished
		admission.adm.setStatus(EncounterStateEnum.FINISHED);		 
		boolean updateSuccess = admissionSystem.updateEncounter(client, admission.adm);		
		
		
		beginningEncounter.setSubject(admission.visit.getSubject());
		ResourceReferenceDt visitReference = new ResourceReferenceDt(admission.visit);
		beginningEncounter.setPartOf(visitReference);
		
		if(discharge)
		{
			beginningHospitalization.setDischargeDisposition(new CodeableConceptDt("http://hl7.org/fhir/discharge-disposition", "home"));
		} 
		else
		{
			beginningHospitalization.setDestination(targetOrganizationReference);
		}

		beginningEncounter.setHospitalization(beginningHospitalization);
				
		admissionSystem.createEncounter(client, beginningEncounter);
		admission.adm = beginningEncounter;

		return updateSuccess;
	}
	
	
    @Override
    public IdDt getHospitalID()
    {
        return infrastructure.getHospitalID(); 
    }
	
    
	@Override
	public Organization getBirthStation() {
		return infrastructure.getBirthStation(); 
	}

	
	@Override
	public Organization getIMC() {		
		return infrastructure.getIMC(); 
	}
	
	@Override
	public Organization getNICU() {		
		return infrastructure.getNICU(); 
	}

	@Override
	public List<Practitioner> getNurseList() {
		return nurseCreation.getAllNurses();		
	}

	
	@Override 
	public List<Practitioner> getDoctorList() {
		return doctorCreation.getAllDoctors();
	}
	
	
	@Override
	public List<Patient> getPatientList() {		
		return patientCreation.getAllPatients();
	}
	

	@Override
	public boolean updatePatient(Patient patient) {
		
		return MyPatient.updatePatient(client, patient);		
	}

	
	// TODO: Create external practitioner
	public void createComposition(AdmissionContainer admissionContainer)
	{
		Composition composition = new Composition();
		
		IdDt externalDoctorId = new IdDt();
		externalDoctorId = doctorCreation.createDoctor("http://www.kh-hh.de/mio/practitioner", 
							"123456", "Schroeder", "Gerhard", "Musterstrasse 2", "22113", 
							"Hamburg", AdministrativeGenderCodesEnum.M);
		
		
		composition.setTitle("Arztbrief");

		// TODO: Unsure whether these are the correct codes!
		composition.setClassElement(new CodeableConceptDt("http://loinc.org", "11495-9"))
					.setTitle("Physical Therapy Initial Assessment Note At First Encounter");
		composition.setType(new CodeableConceptDt("http://loinc.org", "34763-3"));
		
		
		composition.setStatus(CompositionStatusEnum.FINAL);
		composition.setConfidentiality(new CodingDt("http://hl7.org/fhir/v3/Confidentiality", "R"));
		
		composition.addAuthor().setReference(externalDoctorId);
		composition.setSubject(new ResourceReferenceDt(admissionContainer.patient.getId()));
		composition.setEncounter(new ResourceReferenceDt(admissionContainer.visit.getId()));
		composition.setCustodian(new ResourceReferenceDt(hospitalID));

		IdDt compositionId = uploadComposition(client, composition);
		
		admissionContainer.composition = composition;
		
		compositionIds.add(compositionId);
	}
	
	/**
	 * Get all known encounters for a given patient
	 * @param patient
	 * @return
	 */
	@Override
	public List<Encounter> getAllPatientEncounters(Patient patient)
	{
		List<Encounter> resultEncounters = new ArrayList<Encounter>();
		
		// Get all encounters with the patient as subject
		Bundle bundle = client.
				search().
				forResource(Encounter.class)
				.where(Encounter.SUBJECT.hasId(patient.getId()))
				.execute();
		
		resultEncounters.addAll(bundle.getResources(Encounter.class));

		while (!bundle.getLinkNext().isEmpty()) {
			bundle = client
					.loadPage()
					.next(bundle)
					.execute();
			resultEncounters.addAll(bundle.getResources(Encounter.class));
		}
		
		return resultEncounters;
	}
	
	
	@Override
	public void clearEntries() {
		
		// Remove nurses
        nurseCreation.removeAllNurses();
        
        // Remove doctors
        doctorCreation.removeAllDoctors();
        
		// Remove default patients
		patientCreation.removeAllPatients();
				
		// Remove default infrastructure
        infrastructure.wipeInfrastructure(client);
        
        // Remove all encounters 
        admissionSystem.removeAllEncounters();
        
        // Remove all compositions
        for(IdDt compositionID : compositionIds)
        {           
            client
            .delete()
            .resourceById(compositionID)
            .execute();     
        }       
	}
	

	/**
	 * This method creates a composition on the server using the specified IGenericClient and 
	 * retrieves the technical ID. The ID is then shortened and stripped off the appended version
	 * 
	 * @param client
	 * @param composition
	 * @return The non-versioned technical ID of the location
	 */
	private IdDt uploadComposition(IGenericClient client, Composition composition)
	{
		
		MethodOutcome  outcome = client.create().resource(composition).prettyPrint().encodedXml().execute();
		IdDt id = outcome.getId();
		String idPart = id.getIdPart();
		String elementSpecificId = id.getBaseUrl();
		IdDt idNonVersioned = new IdDt(elementSpecificId + "/" + id.getResourceType() + "/" + idPart);

        // Set ID on local patient object
		composition.setId(idNonVersioned);         
		
		return idNonVersioned;		
	}
}
