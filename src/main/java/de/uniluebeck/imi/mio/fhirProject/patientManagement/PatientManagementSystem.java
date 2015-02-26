package de.uniluebeck.imi.mio.fhirProject.patientManagement;

import java.util.List;

import ca.uhn.fhir.context.FhirContext;
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
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.model.dstu.resource.Composition;

public class PatientManagementSystem implements IPatientManagementSystem{

	private FhirContext context;
	private IGenericClient client;
	
	private InfrastructureCreation infrastructure;
	private PatientCreation patientCreation;
	private DoctorCreation doctorCreation;
	private NurseCreation nurseCreation;
	
	private AdmissionSystem admissionSystem;
	
	private IdDt hospitalID;
	
	private List<IdDt> compositionList;
		
	
	public PatientManagementSystem(FhirContext inContext, IGenericClient inClient)
	{
		this.context = inContext;
		this.client = inClient;
		
		//Create default infrastructure
		infrastructure = new InfrastructureCreation(inContext, inClient);
		
		// Get hospitalID from infrastructure
		hospitalID = infrastructure.getHospitalID();
		
		// Create default patients
		patientCreation = new PatientCreation(context, client);
          
		// Create doctors
		doctorCreation = new DoctorCreation(client);
		
		// Create nurses
		nurseCreation = new NurseCreation(client);
	
		// Create admission system
		admissionSystem = new AdmissionSystem(context, client);		
	}
	

    @Override
    public IdDt getHospitalID()
    {
        return infrastructure.getHospitalID(); 
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
			
		// TODO: Note: Arztbrief is implemented (createComposition)
				
		return admission;
	}
	
	@Override
	public Organization getBirthStation() {
		return infrastructure.getBirthStation(); 
	}

	@Override
	public Organization getIMC() {		
		return infrastructure.getIMC(); 
	}
	
	
	// TODO: Create external practitioner
	public List<IdDt> createComposition(AdmissionContainer admissionContainer)
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

		IdDt compositionId = infrastructure.uploadComposition(client, composition);
		
		admissionContainer.composition = composition;
		
		compositionList.add(compositionId);
		
		//TODO write delete-function if necessary
		
		return compositionList;

	}

	

	@Override
	public List<Practitioner> getNurses() {
		return nurseCreation.getAllNurses();		
	}

	@Override
	public List<Patient> getPatientList() {		
		return patientCreation.getAllPatients();
	}

	@Override
	public boolean dischargePatient(AdmissionContainer admission) {
		
		admission.visit.setStatus(EncounterStateEnum.FINISHED);
		admission.adm.setStatus(EncounterStateEnum.FINISHED);
		
		// TODO Ensure that server is updated
		
		Encounter dischargeEncounter = new Encounter();
		
		executePatientTransfer(admission, null, dischargeEncounter, admission.hospitalization, true);
		
		// TODO Fix return value
		return true;
	}

	@Override
	public boolean updatePatient(Patient patient) {
		
		MyPatient.updatePatient(client, patient);
		
		// TODO Fix return value		
		return true;
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

		beginningEncounter.setReason(EncounterReasonCodesEnum.valueOf(transferContainer.diagnosisICD));
		
		//TODO: add narrative (diagnosisDescription) to encounter reason	
						
		return executePatientTransfer(admission, transferContainer, beginningEncounter, beginningHospitalization,
				false);
	}

	
	private boolean executePatientTransfer(AdmissionContainer admission, 
			TransferContainer transferContainer,
			Encounter beginningEncounter,
			Hospitalization beginningHospitalization,
			boolean discharge)
	{
		admission.adm.setStatus(EncounterStateEnum.FINISHED);
		
		beginningEncounter.setSubject(admission.visit.getSubject());
		ResourceReferenceDt visitReference = new ResourceReferenceDt(admission.visit);
		beginningEncounter.setPartOf(visitReference);
		
		if(discharge)
		{
			//admission.visit.setStatus(EncounterStateEnum.FINISHED); //already done in discharge
			
			beginningHospitalization.setDischargeDisposition(new CodeableConceptDt("http://hl7.org/fhir/discharge-disposition", "home"));
		} else
		{
			beginningHospitalization.setDestination(transferContainer.targetOrganizationReference);
		}
		
		// TODO Update encounters on server 
		
		beginningEncounter.setHospitalization(beginningHospitalization);
		
		// TODO: Store id of encounter in AdmissionSystem
		admissionSystem.createEncounter(client, beginningEncounter);
		admission.adm = beginningEncounter;
	
		// TODO fix return value
		return true;
	}
	
	@Override
	public void clearEntries() {

		// TODO etc
		
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
	}
}
