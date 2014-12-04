package patientManagement;

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
import ca.uhn.fhir.rest.client.IGenericClient;


// Absolutely work-in-progress!
public class PatientManagementSystem implements IPatientManagementSystem{

	private FhirContext context;
	private IGenericClient client;
	private InfrastructureCreation infrastructure;
	
	public PatientManagementSystem(FhirContext inContext, IGenericClient inClient)
	{
		this.context = inContext;
		this.client = inClient;
		
		//Create default infrastructure
		infrastructure = new InfrastructureCreation(inContext, inClient);
		
		//TODO Set global ID for hospital.
	}
	/*
	@Override
	public boolean planEncounter(ResourceReferenceDt patientReference, String diagnosisICD,
			ResourceReferenceDt stationReference) {
		
		Encounter plannedEncounter = new Encounter();
		plannedEncounter.setSubject(patientReference);
		
		
		MyCondition indication = new MyCondition(client, client., diagnosisICD, diagnosisText)
		plannedEncounter.setIndication(theValue)
		return false;
	}
	*/
	
	
	@Override
	public Organization getBirthStation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Organization getIMC() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AdmissionContainer admitPatient(PatientCreationParameters patient,
			AdmissionParameters parameters) {
	    
	    return new MyEntering(client, context, patient, parameters).getAdmission();
	}

	@Override
	public List<Practitioner> getNurses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Patient> getPatientList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean dischargePatient(AdmissionContainer admission) {
		
		admission.visit.setStatus(EncounterStateEnum.FINISHED);
		
		Encounter dischargeEncounter = new Encounter();
		
		executePatientTransfer(admission, null, dischargeEncounter, admission.hospitalization, true);
		
		return true;
	}

	@Override
	public boolean updatePatient(Patient patient) {
		
		MyPatient.updatePatient(client, patient);
		
		return false;
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
//				beginningEncounter.setReason() <-------- TODO INSERT Snomed CT value here.
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
		
		
		beginningEncounter.setSubject(admission.visit.getSubject());
		ResourceReferenceDt visitReference = new ResourceReferenceDt(admission.visit);
		beginningEncounter.setPartOf(visitReference);
		
		if(discharge)
		{
			admission.visit.setStatus(EncounterStateEnum.FINISHED);
			
			beginningHospitalization.setDischargeDisposition(new CodeableConceptDt("http://hl7.org/fhir/discharge-disposition", "home"));
		} else
		{
			beginningHospitalization.setDestination(transferContainer.targetOrganizationReference);
		}
		
		beginningEncounter.setHospitalization(beginningHospitalization);
		infrastructure.uploadEncounter(client, beginningEncounter);
		admission.adm = beginningEncounter;
	
		
		return false;
	}
	
	@Override
	public List<Encounter> getAllPatientEncounters(Patient patient) {
		// TODO Auto-generated method stub
		return null;
	}
	


	@Override
	public void clearEntries() {
		// TODO Auto-generated method stub
		
	}




}
