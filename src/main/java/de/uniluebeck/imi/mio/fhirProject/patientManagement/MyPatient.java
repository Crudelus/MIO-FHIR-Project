package de.uniluebeck.imi.mio.fhirProject.patientManagement;


import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.NameUseEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;

/**
 * Class for Patients
 * creates a Patient
 * minimum arguments are: name
 * optional: gender, birthDate, address,maritalStatus
 * only set: deceased, multipleBirth,maganingOrganization, careProvider
 * @author Anne
 *
 */
public class MyPatient {

    // TODO: Setter and Getter if needed!!
    
    // can be given with constructor
    private String firstName;
    private String lastName;
    private String maidenName;
    private AdministrativeGenderCodesEnum gender;
    private DateTimeDt birthDate;
    private String line;
    private String zip;
    private String city;
    private MaritalStatusCodesEnum maritalStatus;
    
    //private ResourceReferenceDt managingOrganization;
    
    // new Patient
    private Patient patient = new Patient();
    
    public MyPatient(PatientCreationParameters patParams) {
    
        this.firstName = patParams.firstName;
        this.lastName = patParams.lastName;
        this.maidenName = patParams.maidenName;
        this.gender = patParams.gender;
        this.birthDate = patParams.birthDate;
        this.line= patParams.line;
        this.zip = patParams.zip;
        this.city = patParams.city;
        this.maritalStatus = patParams.maritalStatus;
        
        patient.addName().addFamily(lastName).addGiven(firstName).setUse(NameUseEnum.OFFICIAL);
        patient.addName().addFamily(maidenName).addGiven(firstName).setUse(NameUseEnum.MAIDEN);
        
        patient.addIdentifier("http://kh-hh.de/mio/patients","Pat-"+(int)(Math.random()*1000));
        patient.setGender(gender);
        patient.setBirthDate(birthDate);
        
        patient.addAddress().addLine(line).setZip(zip).setCity(city).setUse(AddressUseEnum.HOME);
        patient.setMaritalStatus(maritalStatus);
        setNarrative();
    }
   
    
    /**
     * Returns the patient object 
     * @return Patient Object
     */
    public Patient getPatientObj(){
        return patient;
    }
    
    
    /**
     * sets the narrative element to make the patient human readable
     */
    private void setNarrative(){
    NarrativeDt display = new NarrativeDt();
    display.setDiv(patient.getNameFirstRep().getPrefixAsSingleString().toUpperCase()+", "
            +patient.getNameFirstRep().getFamilyAsSingleString().toUpperCase()
            +", "+patient.getNameFirstRep().getGivenAsSingleString()+", "
            +patient.getNameFirstRep().getSuffixAsSingleString()+"-"
            +patient.getIdentifierFirstRep().getElementSpecificId());
    patient.setText(display);
    
    }
    
    
    /**
     * creating current nurse
     * 
    * @param nurse current Practitioner to create
    * @param client server base
    * @return IdDt of updated nurse
    */
    public static IdDt createPatient(IGenericClient client, Patient patient)
    {
		MethodOutcome  outcome = client
				.create()
				.resource(patient)
				.prettyPrint()
				.encodedXml()
				.execute();
		
        IdDt id = outcome.getId();
        String elementSpecificId = id.getBaseUrl();
        String idPart = id.getIdPart();
        IdDt idNonVersioned = new IdDt(elementSpecificId+"/"+id.getResourceType()+"/"+idPart);
        
        // Set ID on local patient object
        patient.setId(idNonVersioned);         
        
        return idNonVersioned;
    } 
     
     /**
      * Updating current nurse
      * 
      * @param nurse current Practitioner to be up to date
      * @param client  server base
      * @param oldIDs List of existing nurse-IDs
      * @param j current index for iterating through oldIDs
      * @return IdDt of updated nurse
      */
     public static void updatePatient(IGenericClient client, Patient patient){
        // set current nurse ID for updating
//           patient.setId(patient.getId());
            
        //MethodOutcome outcome = client
             client
           .update()
           .resource(patient)
           .execute();
        //IdDt id = outcome.getId();
        // use for nonversioned id:
            //String elementSpecificId = id.getBaseUrl();
            //String idPart = id.getIdPart();
            //IdDt idNonVersioned = new IdDt(elementSpecificId+"/"+id.getResourceType()+"/"+idPart);
        //return id;
     }
    
    public void setBirthDate(DateTimeDt birthDate) 
    {
        this.birthDate = birthDate;
    }
    
    /*
    public void setManagingOrganization(ResourceReferenceDt managingOrganization) 
    {
        this.managingOrganization = managingOrganization;
    }
    */
}
