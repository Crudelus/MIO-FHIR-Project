package de.uniluebeck.imi.mio.fhirProject.patientManagement;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.model.dstu.valueset.PractitionerRoleEnum;
import ca.uhn.fhir.model.dstu.valueset.PractitionerSpecialtyEnum;
import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.NameUseEnum;
import ca.uhn.fhir.model.dstu.valueset.NarrativeStatusEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;

public class NurseCreation {

	private List<IdDt> nurseIds;
	private IGenericClient client;

	public NurseCreation(IGenericClient inClient) {
		
		/**
		 * generates nurses as given in the table below one can choose between
		 * "CREATING" new nurses on the server (at the same time, the old ones
		 * are getting deleted) and "UPDATING" existing nurses all IDs of
		 * existing Nurses are stored in the file "nurseIDs.txt",
		 * 
		 * TODO: to make the program work you have to CHANGE THE FILE PATH!!
		 * (Lines 80, 86) TODO: later on, we have to change the
		 * ResourceReferences for organizaton and location (Lines 45,46), TODO:
		 * their settings are commented yet (Lines 191, 194)
		*/

		client = inClient;
		nurseIds = new ArrayList<IdDt>(); 
		
		// dummy organization und location
		ResourceReferenceDt organizationID = new ResourceReferenceDt();
		ResourceReferenceDt locationID = new ResourceReferenceDt();

		/*
		 * creates a list out of single nurses: Trennzeichen:
		 * Felder(;),Wiederholung(,),Eintr�ge(^), zusammengeh�rige Eintr�ge (')
		 * Felder: Eintr�ge name: use (1:usual, 2:official, 3:temp, 4:nickname,
		 * 5:anonymous, 6:old, 7:maiden), family given middle gender (1:Female,
		 * 2:male, 3:undifferentiated, 4:unknown) birthDate address: use
		 * (1:home, 2:work, 3:temp, 4:old), line zip city specialty
		 * (1:Cardiologist, 2:Dentist, 3:Dietary consultant, 4:Midwife, 5:System
		 * architect) organization location
		 */
		List<String> nurseStrings = new ArrayList<String>();
		// use^fName^gName^mName gender birthdate use^line^zip^city specialty
		nurseStrings.add("2^Piazza^Laurine^Emily;" + "1;" + "1973-03-21;"
				+ "1^Flotowstr. 27^20095^HAMBURG;" + "4;" + organizationID
				+ ";" + locationID);
		nurseStrings.add("2^Espinosa^Carla;" + "1;" + "1985-07-01;"
				+ "1^Hans-Grade-Allee 42^22045^HAMBURG,"
				+ "3^Berliner Str. 42^20096^HAMBURG;" + "4;" + organizationID
				+ ";" + locationID);
		nurseStrings.add("2^Lehmann^Marc;" + "2;" + "1977-04-14;"
				+ "1^Budapester Str. 44^20099^HAMBURG;" + "4;" + organizationID
				+ ";" + locationID);
		nurseStrings.add("2^Koenigsmann^Eleonora;" + "1;" + "1981-11-05;"
				+ "1^Knesebeckstr. 10^21029^HAMBURG;" + "4;" + organizationID
				+ ";" + locationID);
		nurseStrings.add("2^Augustin^Doran;" + "2;" + "1969-01-11;"
				+ "1^Konstanzer Str. 36^22043^HAMBURG;" + "4;" + organizationID
				+ ";" + locationID);
		nurseStrings.add("2^Van'Daal^Elva;" + "1;" + "1979-10-30;"
				+ "1^Ellmenreichstr. 22^22761^HAMBURG;" + "4;" + organizationID
				+ ";" + locationID);
		nurseStrings.add("2^Wanthenhaeuser^Edna;" + "1;" + "1952-09-21;"
				+ "1^Strassenstrasse. 5^22761^HAMBURG;" + "4;" + organizationID
				+ ";" + locationID);
		
		// creating Nurses with FHIR
		createNurses(nurseStrings);
	}

	// Delete all nurse entries on our hospital
    public void removeAllNurses()
    {	    	
    	for(IdDt patientID : nurseIds)
    	{    		
    		client
			.delete()
			.resourceById(patientID)
			.execute();		
    	}			
    }
    
    
    
	
	/**
	 * Method to create nurses out of a given list containing name, gender,
	 * address, specialty etc.
	 * 
	 * @param nurse
	 *            List out of "nurses", every entry represents a nurse with its
	 *            properties
	 * @param create
	 *            (boolean): true, if new Nurses have to be created (delete the
	 *            old ones); false, if existing ones just have to get updated
	 * @param oldIDs
	 *            List out of IDs for existing nurses which have to be updated
	 *            or deleted, respectively
	 * @return List of IdDt of created and uploaded nurses
	 */
	public void createNurses(List<String> nurse){	
		
		// iterate over the entries of the given list
		int j = 0;
		for (String entry : nurse) {
			String[] n = entry.split(";");

			// creates new nurse
			Practitioner pfleger = new Practitioner();

			// adds identifier to nurse
			j++;
			pfleger.addIdentifier("http://kh-hh.de/mio/practitioners",
					"MIO-9376" + j);

			// birthdate of nourse in right format
			pfleger.setBirthDate(new DateTimeDt(n[2]));

			// extract names: official, usual, temp,...
			String[] name = n[0].split("\\^");

			HumanNameDt nurseName = new HumanNameDt();
			switch (Integer.parseInt(name[0])) {
			case 1:
				nurseName.setUse(NameUseEnum.USUAL);
				break;
			case 2:
				nurseName.setUse(NameUseEnum.OFFICIAL);
				break;
			case 3:
				nurseName.setUse(NameUseEnum.TEMP);
				break;
			case 4:
				nurseName.setUse(NameUseEnum.NICKNAME);
				break;
			case 5:
				nurseName.setUse(NameUseEnum.ANONYMOUS);
				break;
			case 6:
				nurseName.setUse(NameUseEnum.OLD);
				break;
			case 7:
				nurseName.setUse(NameUseEnum.MAIDEN);
				break;
			default:
				break;
			}
			HumanNameDt nName = createName(nurseName, name);
			pfleger.setName(nName);

			// human readable
			NarrativeDt description = new NarrativeDt();
			description.setDiv("<div><p>"
					+ pfleger.getName().getGivenAsSingleString() + " "
					+ pfleger.getName().getFamilyAsSingleString().toUpperCase()
					+ "(" + pfleger.getId() + ")</p></div>");
			description.setStatus(NarrativeStatusEnum.GENERATED);
			pfleger.setText(description);

			// extract single entries of the home address
			String[] add = n[3].split(",");
			String[] address = add[0].split("\\^"); // erstmal nur die erste
			switch (Integer.parseInt(address[0])) { // specify use
			case 1:
				AddressDt addH = new AddressDt();
				addH.setUse(AddressUseEnum.HOME);
				AddressDt addh = createAddress(addH, address);
				pfleger.setAddress(addh);
				break;
			case 2:
				AddressDt addW = new AddressDt();
				addW.setUse(AddressUseEnum.WORK);
				AddressDt addw = createAddress(addW, address);
				pfleger.setAddress(addw);
				break;
			case 3:
				AddressDt addT = new AddressDt();
				addT.setUse(AddressUseEnum.TEMP);
				AddressDt addt = createAddress(addT, address);
				pfleger.setAddress(addt);
				break;
			case 4:
				AddressDt addO = new AddressDt();
				addO.setUse(AddressUseEnum.OLD);
				AddressDt addo = createAddress(addO, address);
				pfleger.setAddress(addo);
				break;
			default:
				break;
			}

			// adds hospital to nurse
			// pfleger.setOrganization(new ResourceReferenceDt(n[5]));

			// where you can find the nurse in hospital
			// ResourceReferenceDt loc = pfleger.addLocation(new
			// ResourceReferenceDt(n[6]););

			// Gender of nurse
			switch (Integer.parseInt(n[1])) {
			case 1:
				pfleger.setGender(AdministrativeGenderCodesEnum.F);
				break;
			case 2:
				pfleger.setGender(AdministrativeGenderCodesEnum.M);
				break;
			case 3:
				pfleger.setGender(AdministrativeGenderCodesEnum.UN);
				break;
			case 4:
				pfleger.setGender(AdministrativeGenderCodesEnum.UNK);
				break;
			default:
				break;
			}

			// adds a role to nurse
			pfleger.addRole(PractitionerRoleEnum.NURSE);

			// adds specialty to nurse
			int specialty = Integer.parseInt(n[4]);
			switch (specialty) {
			case 1:
				pfleger.addSpecialty(PractitionerSpecialtyEnum.CARDIOLOGIST);
				break;
			case 2:
				pfleger.addSpecialty(PractitionerSpecialtyEnum.DENTIST);
				break;
			case 3:
				pfleger.addSpecialty(PractitionerSpecialtyEnum.DIETARY_CONSULTANT);
				break;
			case 4:
				pfleger.addSpecialty(PractitionerSpecialtyEnum.MIDWIFE);
				break;
			case 5:
				pfleger.addSpecialty(PractitionerSpecialtyEnum.SYSTEMS_ARCHITECT);
				break;
			default:
				break;
			}			
			
			IdDt nurseId  = createNurse(pfleger, client);
			nurseIds.add(nurseId);
		}	
	}

	public List<Practitioner> getAllNurses()
    {
    	List<Practitioner> nurses = new ArrayList<Practitioner>();
    	
    	for(IdDt nurseID : nurseIds)
    	{    		
    		Practitioner nurse = client.read(Practitioner.class, nurseID);
    		nurses.add(nurse);
    	}
    	
    	return nurses;
    }
	
	/**
	 * Updating current nurse
	 * 
	 * @param nurse
	 *            current Practitioner to be up to date
	 * @param client
	 *            server base
	 * @param oldIDs
	 *            List of existing nurse-IDs
	 * @param j
	 *            current index for iterating through oldIDs
	 * @return IdDt of updated nurse
	 */
	/*
	public static IdDt updateNurse(Practitioner nurse, IGenericClient client,
			String oldID) {
		// set current nurse ID for updating
		nurse.setId(oldID);

		MethodOutcome outcome = client.update().resource(nurse).execute();
		IdDt id = outcome.getId();
		// use for nonversioned id:
		// String elementSpecificId = id.getBaseUrl();
		// String idPart = id.getIdPart();
		// IdDt idNonVersioned = new
		// IdDt(elementSpecificId+"/"+id.getResourceType()+"/"+idPart);
		return id;
	}
	*/
	/**
	 * creating current nurse
	 * 
	 * @param nurse
	 *            current Practitioner to create
	 * @param client
	 *            server base
	 * @return IdDt of updated nurse
	 */
	public static IdDt createNurse(Practitioner nurse, IGenericClient client) {
		MethodOutcome outcome = client.create().resource(nurse).prettyPrint()
				.encodedJson().execute();
		IdDt id = outcome.getId();
		// use for nonversioned id:
		// String elementSpecificId = id.getBaseUrl();
		// String idPart = id.getIdPart();
		// IdDt idNonVersioned = new
		// IdDt(elementSpecificId+"/"+id.getResourceType()+"/"+idPart);
		return id;
	}

	/**
	 * Method to add names to nurse creates a text for human readability
	 * 
	 * @param objName
	 *            Object of Type HumanNameDt which we want to modify
	 * @param name
	 *            Array filled with all given and family names
	 * @return modified Object
	 */
	public static HumanNameDt createName(HumanNameDt objName, String[] name) {

		for (String fName : name[1].split("\\'")) {
			objName.addFamily(fName);
		}
		for (int i = 2; i < name.length; i++) {
			objName.addGiven(name[i]);
		}
		objName.setText(objName.getFamilyAsSingleString().toUpperCase() + ", "
				+ objName.getGivenAsSingleString());
		return objName;
	}

	/**
	 * Method to add addresses to nurse creates a text for human readability
	 * 
	 * @param objAdd
	 *            Object of Type AddressDt which we want to modify
	 * @param add
	 *            Array filled with line, zip and city
	 * @return modified Object
	 */
	public static AddressDt createAddress(AddressDt objAdd, String[] add) {
		StringDt line = objAdd.addLine();
		line.setValueAsString(add[1]);
		objAdd.setZip(add[2]);
		objAdd.setCity(add[3]);
		objAdd.setText(objAdd.getUse() + ": " + objAdd.getLineFirstRep() + ", "
				+ objAdd.getZip() + " " + objAdd.getCity());
		return objAdd;
	}
}
