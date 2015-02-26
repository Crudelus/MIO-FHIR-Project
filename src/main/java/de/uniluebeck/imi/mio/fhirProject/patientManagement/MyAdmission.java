package de.uniluebeck.imi.mio.fhirProject.patientManagement;

import java.text.SimpleDateFormat;
import java.util.Date;

import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.Encounter.Hospitalization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.EncounterClassEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterStateEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;

/**
 * Class for admissions
 * creates an Encounter in admission-Style
 * minimum arguments are: client, patient(Patient/IdDt),admissionClass, Indication
 * optional: admitting doctor (Practitioner/IdDt)
 * @author Anne
 *
 */
public class MyAdmission {

    
    private Encounter admission = new Encounter();
    private Encounter visit = new Encounter();
    private Hospitalization hosp = new Hospitalization();
    
    // current date
    Date date = java.util.Calendar.getInstance().getTime();
    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    String dateString = dateFormatter.format(date);
    
    /**
     * Admission of new Patient, including an admitting practitioner
     * @param client
     * @param patient: patient of Type Patient
     * @param admClass: integer (1-8)
     * @param indication
     * @param docId: Id of doctor
     */
    public MyAdmission(IGenericClient client, 
	    		Patient patient, 
	    		EncounterClassEnum admClass, 
	    		IdDt indicationID, 
	    		IdDt docId,
	    		IdDt station,
	    		IdDt hospital,
	    		boolean reAdmission) {
	
	System.out.println("###### 5. Entcounter zur Aufnahme wird erstellt...");
	
	
	// big encounter to record the whole visit of the patient
	visit.addIdentifier("http://kh-hh.de/mio/encounters","Visit-"+(int)(Math.random()*1000));
	visit.setStatus(EncounterStateEnum.IN_PROGRESS);
	visit.setClassElement(admClass);
//	giveAdmClass(admClass);
	visit.setSubject(new ResourceReferenceDt(patient.getId()));
	visit.addParticipant().setIndividual(new ResourceReferenceDt(docId));	
	visit.setPeriod(new PeriodDt().setStart(new DateTimeDt(dateString)));
	visit.setIndication(new ResourceReferenceDt(indicationID));
	
	
	// small encounter for admission of the patient: part of the visit-Encounter
	admission.addIdentifier("http://kh-hh.de/mio/encounters","Adm-"+(int)(Math.random()*1000));
	admission.setStatus(EncounterStateEnum.FINISHED);
	admission.setClassElement(admClass);
	admission.setSubject(new ResourceReferenceDt(patient.getId()));
	admission.addParticipant().setIndividual(new ResourceReferenceDt(docId));	
	admission.setPeriod(new PeriodDt().setStart(new DateTimeDt(dateString)));
	admission.setIndication(new ResourceReferenceDt(indicationID));
        hosp.addAccomodation().setBed(new ResourceReferenceDt(station));
        hosp.setReAdmission(reAdmission); // accepts boolean and sets it true, if the patient was admitted one more time
        admission.setHospitalization(hosp);       
    admission.setServiceProvider(new ResourceReferenceDt(hospital));
	
	System.out.println("###### 5.2. Narrativer Text wird erstellt.");
	setNarrative(patient);
	
	
	System.out.println("###### 5.3. Encounter wird auf den Server geladen...");
        MethodOutcome outcomeV = client
                .create()
                .resource(visit)
                .prettyPrint()
                .encodedJson()
                .execute();
        IdDt idV = outcomeV.getId();
        System.out.println("###### 5.2. Visit-ID: "+idV);
        
        
        String elementSpecificIdV = idV.getBaseUrl();
        String idPartV = idV.getIdPart();
        IdDt idNonVersionedV = new IdDt(elementSpecificIdV+"/"+idV.getResourceType()+"/"+idPartV);
        visit.setId(idNonVersionedV);  
        admission.setPartOf(new ResourceReferenceDt(idNonVersionedV));
	
        MethodOutcome outcomeA = client
                .create()
                .resource(admission)
                .prettyPrint()
                .encodedJson()
                .execute();
        IdDt idA = outcomeA.getId();
        System.out.println("###### 5.2. Admission-ID: "+idA);
        
        
        String elementSpecificIdA = idA.getBaseUrl();
        String idPartA = idA.getIdPart();
        IdDt idNonVersionedA = new IdDt(elementSpecificIdA+"/"+idA.getResourceType()+"/"+idPartA);
        admission.setId(idNonVersionedA);   
        
        
        
        
	System.out.println("###### Encounter erfolgreich erstellt.");
    }
    

    private void setNarrative(Patient patient){
	NarrativeDt display = new NarrativeDt();
	display.setDiv("Aufnahme: "+admission.getIdentifier()
			+" des Patienten "+patient.getNameFirstRep().getFamilyAsSingleString().toUpperCase()+", "
			+ patient.getNameFirstRep().getGivenAsSingleString());
	admission.setText(display);
    }

    
    /**
     * asks for the encounter object 
     * @return admission Object
     */
    public Encounter getAdmObj(){
	return admission;
    }
    
    /**
     * asks for the encounter object 
     * @return visit Object
     */
    public Encounter getVisitObj(){
	return visit;
    }
    
    public Hospitalization getHospitalization(){
	return hosp;
    }
}
