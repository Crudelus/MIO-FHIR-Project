package patientManagement;


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
//    private List<MyAddress> address;
    private String line;
    private String zip;
    private String city;
    private MaritalStatusCodesEnum maritalStatus;
    
    // can only be set without constructor
//    private boolean deceased;
//    private boolean multipleBirth;

    private ResourceReferenceDt managingOrganization;
    //private ResourceReferenceDt careProvider;
    
    // new Patient
    private Patient patient = new Patient();
    
    // Constructors:  
//    public MyPatient(String firstName,
//	    		String lastName,
//	    		String maidenName,
//	    		AdministrativeGenderCodesEnum gender, 
//	    		DateTimeDt birthDate, 
//	    		String line,
//	    		String zip,
//	    		String city, 
//	    		MaritalStatusCodesEnum maritalStatus) {
    
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
//	for (MyName nameSingle:name){	
//	    givePatName(patient.addName(), nameSingle);
//	}
	
	patient.addIdentifier("http://kh-hh.de/mio/patients","Pat-"+(int)(Math.random()*1000));
	patient.setGender(gender);
//	givePatGender(gender);
	patient.setBirthDate(birthDate);

	
	patient.addAddress().addLine(line).setZip(zip).setCity(city).setUse(AddressUseEnum.HOME);
//	for (MyAddress addSingle:address){
//	    givePatAddress(patient.addAddress(), addSingle);
//	}
	
//	givePatMaritalStatus(maritalStatus);
	patient.setMaritalStatus(maritalStatus);
	setNarrative();
    }
   
    
    /**
     * asks for the patient object 
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
    public static IdDt createPatient(IGenericClient client, Patient patient){
        MethodOutcome outcome = client
            .create()
            .resource(patient)
            .prettyPrint()
            .encodedJson()
            .execute();
        IdDt id = outcome.getId();
        String elementSpecificId = id.getBaseUrl();
        String idPart = id.getIdPart();
        IdDt idNonVersioned = new IdDt(elementSpecificId+"/"+id.getResourceType()+"/"+idPart);
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
//	    	 patient.setId(patient.getId());
	    	
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
    
//    /**
//     * filling the HumanNameDt object
//     * Mapping of use of name to FHIR code
//     * @param humanName
//     */
//    private void givePatName(HumanNameDt humanName, MyName name){
//	for (String f:name.getFamily()){humanName.addFamily(f);	}
//	for (String g:name.getGiven()){	humanName.addGiven(g);	}
//	for (String p:name.getPrefix()){humanName.addPrefix(p);	}
//	for (String s:name.getSuffix()){humanName.addSuffix(s);	}
// 	switch(name.getUse()){
//         	case 1: humanName.setUse(NameUseEnum.USUAL);break;
//         	case 2: humanName.setUse(NameUseEnum.OFFICIAL);break;          			 	
//         	case 3: humanName.setUse(NameUseEnum.TEMP);break;           			 	
//         	case 4: humanName.setUse(NameUseEnum.NICKNAME);break;
//         	case 5: humanName.setUse(NameUseEnum.ANONYMOUS);break;            			 	
//         	case 6: humanName.setUse(NameUseEnum.OLD);break;            			 	
//         	case 7: humanName.setUse(NameUseEnum.MAIDEN);break;
//         	default: break;
//	}
//	humanName.setText(name.getText());
//    }
//    
//    /**
//     * Mapping of gender to FHIR code
//     * @param int gender
//     */
//    private void givePatGender(int gender){
//	switch (gender){
//        	case 1: patient.setGender(AdministrativeGenderCodesEnum.F);break;
//        	case 2: patient.setGender(AdministrativeGenderCodesEnum.M);break;
//        	case 3: patient.setGender(AdministrativeGenderCodesEnum.UN);break;
//        	case 4: patient.setGender(AdministrativeGenderCodesEnum.UNK);break;
//        	default: break;
//	}
//    }
    
//    /**
//     * filling the AddressDt object
//     * @param add
//     */
//    private void givePatAddress(AddressDt add, MyAddress address){
//	add.addLine(address.getLine());	add.setZip(address.getZip());
//	add.setCity(address.getCity());	add.setCountry(address.getCountry());
//	add.setText(address.getLine()+", "+ address.getZip()+", "+ address.getCity()+", " +address.getCountry().toUpperCase());
//	givePatAddressUse(add,address.getUse());
//	
//    }
//    
//    /**
//     * Mapping use of address to FHIR code
//     * @param add object to which you want to add the use
//     * @param int use selects the use for address
//     */
//    private void givePatAddressUse(AddressDt add,int use){
//	 switch (use){ // specify use
//        	 case 1: 
//        	 	   add.setUse(AddressUseEnum.HOME);break;
//        	 case 2: 
//        	 	   add.setUse(AddressUseEnum.WORK);break;
//        	 case 3: 
//        	 	   add.setUse(AddressUseEnum.TEMP);break;
//        	 case 4: 
//        	 	   add.setUse(AddressUseEnum.OLD);break;
//        	 default: break;
//	 }	
//    }
//    
//    /**
//     * Mapping marital status to FHIR code
//     * @param maritalStatus
//     */
//    private void givePatMaritalStatus(int maritalStatus){
//	 switch (maritalStatus){ // specify marital status
//        	 case 1: 
//        	 	patient.setMaritalStatus(MaritalStatusCodesEnum.UNMARRIED);break;
//        	 case 2: 
//        	     	patient.setMaritalStatus(MaritalStatusCodesEnum.A);break; //annulled
//        	 case 3: 
//        	     	patient.setMaritalStatus(MaritalStatusCodesEnum.D);break; //divorced
//        	 case 4: 
//        	     	patient.setMaritalStatus(MaritalStatusCodesEnum.I);break; //interlocutory
//        	 case 5: 
//        	     	patient.setMaritalStatus(MaritalStatusCodesEnum.L);break; //legally separated
//        	 case 6: 
//        	     	patient.setMaritalStatus(MaritalStatusCodesEnum.M);break; //married
//        	 case 7: 
//        	     	patient.setMaritalStatus(MaritalStatusCodesEnum.P);break; //polygamous
//        	 case 8: 
//        	     	patient.setMaritalStatus(MaritalStatusCodesEnum.S);break; //never married
//        	 case 9: 
//        	     	patient.setMaritalStatus(MaritalStatusCodesEnum.T);break; //domestic partner
//        	 case 10: 
//        	     	patient.setMaritalStatus(MaritalStatusCodesEnum.W);break; //widowed
//        	 case 11:
//        	     	patient.setMaritalStatus(MaritalStatusCodesEnum.UNK);break; //unknown
//        	 default: 
//        	     	patient.setMaritalStatus(MaritalStatusCodesEnum.UNK);break; //unknown
//	 }
//    }

    
    
    // getter:
//    public List<MyName> getName() {
//	return name;
//    }
//    public DateTimeDt getBirthDate() {
//	return birthDate;
//    }
//    public int getGenderMap() {
//	return gender;
//    }
//    public String getGender() {
//	switch (gender){
//        	case 1: return "F";
//        	case 2: return "M";
//        	case 3: return "UN";
//        	case 4: return "UNK";
//         	default: return "UNK";
//	}
//    }
////    
//    public List<MyAddress> getAddress() {
//	return address;
//    }
    
//    public int getMaritalStatusMap() {
//	return maritalStatus;
//    }
//    
//    public String getmaritalStatus() {
//	switch (maritalStatus){
//        	case 1: return "Unmarried";
//        	case 2: return "Annulled";
//        	case 3: return "Divorced";
//        	case 4: return "Interlocutory";
//        	case 5: return "Legally Separated";
//        	case 6: return "Married";
//        	case 7: return "Polygamous";
//        	case 8: return "Never Married";
//        	case 9: return "Domestic partner";
//        	case 10: return "Widowed";
//        	case 11: return "Unknown";
//         	default: return "Unknown";
//	}
//    }
    
//    public ResourceReferenceDt getCareProvider() {
//	return careProvider;
//    }
//    
//    public ResourceReferenceDt getManagingOrganization() {
//	return managingOrganization;
//    }
//    
//    public boolean isDeceased(){
//	return deceased;
//    }
//    
//    public boolean isPartofMultipleBirth(){
//	return multipleBirth;
//    }



    
    // Setter:
    
//    public void setName(MyName name, int index) {
//	this.name.add(index,name);
//    }
    
    public void setBirthDate(DateTimeDt birthDate) {
	this.birthDate = birthDate;
    }
    
//    public void setGender(int gender) {
//	this.gender = gender;
//    }
    
//    public void setAddress(MyAddress address, int index) {
//	this.address.add(index,address);
//    }
//    public void setMultipleBirth(boolean multipleBirth) {
//	this.multipleBirth = multipleBirth;
//    }
    
//    public void setMaritalStatus(int maritalStatus) {
//	this.maritalStatus = maritalStatus;
//    }
   
    /*    
    public void setCareProvider(ResourceReferenceDt careProvider) {
	this.careProvider = careProvider;
    }
    */
    
//    public void setDeceased(boolean deceased) {
//	this.deceased = deceased;
//    }
    
    public void setManagingOrganization(ResourceReferenceDt managingOrganization) {
	this.managingOrganization = managingOrganization;
    }


    // Special methods:

}
