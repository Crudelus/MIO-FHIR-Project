package de.uniluebeck.imi.mio.fhirProject.patientManagement;

import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Condition;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.NarrativeStatusEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;

/**
 *  Class for Indications
 * creates an Condition in indication-Style
 * minimum arguments are: patient(Patient/IdDt), ID of the doctor, Name of the doctor, ICD of diagnosis, description of diagnosis
 * @author Anne
 *
 */
public class MyCondition {

	private Condition indication = new Condition();
    
    /**
     * Constructor of an Indication with new Patient
     * @param patient
     * @param docId
     * @param docName
     * @param diagnosisICD
     * @param diagnosisText
     */
    public MyCondition(IGenericClient client, 
    		Patient patient,
    		AdmissionParameters admParams) 
    {		
		indication.addIdentifier("http://kh-hh.de/mio/patients","Cond-"+(int)(Math.random()*1000));
		indication.setSubject(new ResourceReferenceDt(patient.getId()));
		indication.setAsserter(new ResourceReferenceDt(admParams.doctorID));
		indication.setCode(new CodeableConceptDt("http://hl7.org/fhir/sid/icd-10", admParams.diagnosisICD).setText(admParams.diagnosisDescription));
		indication.setCategory(new CodeableConceptDt("http://hl7.org/fhir/condition-category", "finding"));
		
		// Create Narrative 
		NarrativeDt text = new NarrativeDt();
		text.setDiv("Aufnahmediagnose: "+ admParams.diagnosisICD+" ("+ admParams.diagnosisDescription+")\n"
				+"Patient: "+patient.getName()+" ("+patient.getId()+")"
				+"/n Behandelnder Arzt: "+" ("+admParams.doctorID+")");
		text.setStatus(NarrativeStatusEnum.GENERATED);
		indication.setText(text);
	        
		// Upload condition
		createCondition(client, indication);	        
    }

    /**
     * asks for the condition object 
     * @return Patient Object
     */
    public Condition getCondObj(){
    	return indication;
    }
    
    /*
    public static IdDt uploadCondition(IGenericClient client,Condition condition){
        MethodOutcome outcome = client
                .create()
                .resource(condition)
                .prettyPrint()
                .encodedJson()
                .execute();
        IdDt id = outcome.getId();
        
        // use for nonversioned id:
        String elementSpecificId = id.getBaseUrl();
        String idPart = id.getIdPart();
        return new IdDt(elementSpecificId+"/"+id.getResourceType()+"/"+idPart); 
    }
    */
    
    public static boolean updateCondition(IGenericClient client,Condition condition)
    {
    	try {
			client
			.update()
			.resource(condition)
			.execute();
			return true;
		} catch (Exception e) {
			System.err.println("Condition update failed");
			e.printStackTrace();
			return false;
		}    	
    }
    
    
    private IdDt createCondition(IGenericClient client, Condition condition)
    {
		MethodOutcome  outcome = client
				.create()
				.resource(condition)
				.prettyPrint()
				.encodedXml()
				.execute();
		
        IdDt id = outcome.getId();
        String elementSpecificId = id.getBaseUrl();
        String idPart = id.getIdPart();
        IdDt idNonVersioned = new IdDt(elementSpecificId+"/"+id.getResourceType()+"/"+idPart);
        
        // Set ID on local condition object
        condition.setId(idNonVersioned);         
        
        return idNonVersioned;
    }
    
    
    
    /**
     * Constructor of an Indication with new Patient
     * @param patient
     * @param docId
     * @param docName
     * @param diagnosisICD
     * @param diagnosisText
     */
    /*
    public MyCondition(IGenericClient client, 
	    		IdDt encounterId,
	    		String diagnosisICD, 
	    		String diagnosisText) 
    {
	
		indication.addIdentifier("http://kh-hh.de/mio/patients","Cond-"+(int)(Math.random()*1000));
		//indication.setSubject(new ResourceReferenceDt(patient.getId()));
		//indication.setAsserter(new ResourceReferenceDt(docId));
		indication.setCode(new CodeableConceptDt("http://hl7.org/fhir/sid/icd-10" , diagnosisICD).setText(diagnosisText));
		indication.setCategory(new CodeableConceptDt("http://hl7.org/fhir/condition-category", "diagnosis"));
		indication.setEncounter(new ResourceReferenceDt(encounterId));

        indication.setId(uploadCondition(client, indication));
    }
    */
    
    
    /**
     * Constructor of an Indication with existing Patient
     * @param patId
     * @param docId
     * @param docName
     * @param diagnosisICD
     * @param diagnosisText
     */
    /*
    public MyCondition(IGenericClient client,
	    	IdDt patId,
		IdDt docId, 
		String diagnosisICD, 
		String diagnosisText) 
    {
		System.out.println("###### 4. Die Aufnahmediagnose wird erstellt...");
		
	        indication.addIdentifier("http://kh-hh.de/mio/patients","Cond-"+(int)(Math.random()*1000));
	        indication.setSubject(new ResourceReferenceDt(patId));
	        indication.setAsserter(new ResourceReferenceDt(docId));
	        indication.setCode(new CodeableConceptDt("http://hl7.org/fhir/sid/icd-10" , diagnosisICD).setText(diagnosisText));
	        indication.setCategory(new CodeableConceptDt("http://hl7.org/fhir/condition-category", "finding"));
	        
	        NarrativeDt text = new NarrativeDt();
	        text.setDiv("Aufnahmediagnose: "+diagnosisICD+" ("+diagnosisText+")\n"
	        		+"Patient: "+patId
	        		+"/n Behandelnder Arzt: "+" ("+docId+")");
	        text.setStatus(NarrativeStatusEnum.GENERATED);
	        indication.setText(text);
	        
	        
	        System.out.println("4.1. Aufnahmediagnose: "+diagnosisICD+" ("+diagnosisText+")\n"
			+"Patient: "+patId
			+"/n Behandelnder Arzt: "+" ("+docId+")");
	        
		System.out.println("###### 4.1. Aufnahmediagnose auf den Server geladen...");
	        indication.setId(uploadCondition(client, indication));
	        
		System.out.println("###### Aufnahmediagnose erfolgreich erstellt.");
    }
    */
    
}
