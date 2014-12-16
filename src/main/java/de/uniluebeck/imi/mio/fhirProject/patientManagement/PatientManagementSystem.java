package de.uniluebeck.imi.mio.fhirProject.patientManagement;

import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.DurationDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.model.dstu.resource.Encounter.Hospitalization;
import ca.uhn.fhir.model.dstu.valueset.EncounterReasonCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterStateEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.IGenericClient;

public class PatientManagementSystem implements IPatientManagementSystem{

	private FhirContext context;
	private IGenericClient client;
	
	private InfrastructureCreation infrastructure;
	private PatientCreation patientCreation;
	private DoctorCreation doctorCreation;
	private NurseCreation nurseCreation;
	
	private IdDt hospitalID;
	
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
			
	}

	@Override
	public AdmissionContainer planAdmission(PatientCreationParameters patientParameters, 
								 AdmissionParameters admissionParameters) 
	{		
		// TODO: Ensure that the following is done:
		// Create new Admission Container:
		// Create patient if not existing yet
		// Create condition 
		// Create visit with status 'planned'
	
		MyEntering entering = new MyEntering(client, context, patientParameters, admissionParameters, hospitalID, patientCreation ); 
		AdmissionContainer admission = entering.getAdmission();
			
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

	
	// TODO: SOMETHING
	@Override
	public AdmissionContainer admitPatient(AdmissionContainer admission,
							PatientCreationParameters patient,
							AdmissionParameters parameters,
							boolean planned) 
	{    
		// TODO: This must be used to expand the existing admission container
		// TODO: Allow system to do both planning and admission at once for scenario birth
		
		/*
		AdmissionContainer admission = new MyEntering(client, context, patient, parameters, hospitalID).getAdmission(); 
	    		
		if(planned)
		{
			admission.visit.setStatus(EncounterStateEnum.PLANNED);
			// TODO Update server
		}
		
		// Send ID to patientCreation for later deletion
		patientCreation.addPatientToList(admission.patient.getId());
		
		*/
		return admission;
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
		
		//TODO: add narrative (diagnosisDescription) to encouter reason	
						
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
		infrastructure.uploadEncounter(client, beginningEncounter);
		admission.adm = beginningEncounter;
	
		// TODO fix return value
		return true;
	}
	
	@Override
	public void clearEntries() {
				
		// TODO: Remove encounters etc
		
		// Remove nurses
        nurseCreation.removeAllNurses();
        
        // Remove doctors
        doctorCreation.removeAllDoctors();
        
		// Remove default patients
		patientCreation.removeAllPatients();
				
		// Remove default infrastructure
        infrastructure.wipeInfrastructure(client);        
	}
}
