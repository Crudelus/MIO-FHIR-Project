package de.uniluebeck.imi.mio.fhirProject.patientManagement;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.model.dstu.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.NarrativeStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.PractitionerRoleEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;

public class DoctorCreation {

	//class variables
	IGenericClient client;
	
    private List<IdDt> doctorIds;
    
	//Generating the FhirContext-object is expensive. No need to create 
	//this object for every interaction with the server. The object should be
	//created in the main.
	public DoctorCreation(IGenericClient client) {
		
		this.client = client;
		
		doctorIds = new ArrayList<IdDt>();
		createAll();
	}
	
	//This method creates all doctors using createDoctor
	public void createAll() {
		
		createDoctor("http://www.kh-hh.de/mio/practitioner", "1337", "Laser", 
				"Eduard", "Geverdesstraße 9", "23554", "Lübeck", AdministrativeGenderCodesEnum.M);

		createDoctor("http://www.kh-hh.de/mio/practitioner", "1338", "House", 
				"Gregory", "221B Baker Street", "W1U", "London", AdministrativeGenderCodesEnum.M);
	
		createDoctor("http://www.kh-hh.de/mio/practitioner", "1339", "Reid", 
				"Elliot", "Burton St. 15", "90706", "Seattle", AdministrativeGenderCodesEnum.F);

		createDoctor("http://www.kh-hh.de/mio/practitioner", "1340", "Grey", 
				"Meredith", "201 S. Jackson St.", "98104", "Los Angeles", AdministrativeGenderCodesEnum.F);

		createDoctor("http://www.kh-hh.de/mio/practitioner", "1341", "Dog", 
				"Doc", "Hundestraße 12", "23552", "Lübeck", AdministrativeGenderCodesEnum.UNK);
	}
	
	
	//This method creates a doctor. 
	public IdDt createDoctor(String system, String value, String familyName, String givenName, 
			String streetAndNumber, String zip, String city, AdministrativeGenderCodesEnum gender) {
	
		//delete resources with same name
		delete(familyName, givenName);
	
		//specifying attributes: identifier, name, address, gender, role, textual description
		Practitioner doc = new Practitioner();
		
		IdentifierDt arztId = doc.addIdentifier();
		
		HumanNameDt humName = new HumanNameDt();
		humName.addFamily(familyName);
		humName.addGiven(givenName);
		
		AddressDt address = new AddressDt();
		address.addLine(streetAndNumber);
		address.setZip(zip);
		address.setCity(city);
		address.setUse(AddressUseEnum.HOME);
		address.setText(streetAndNumber+", "+zip+" "+city);
		
		NarrativeDt narrative = new NarrativeDt();
		narrative.setDiv("Doktor "+givenName+" "+familyName);
		narrative.setStatus(NarrativeStatusEnum.GENERATED);
	
		UriDt uri = new UriDt();
		uri.setValueAsString(system);
		
		arztId.setSystem(uri);
		arztId.setValue(value);
		doc.setName(humName);
		doc.setAddress(address);
		doc.setGender(gender);
		doc.setRole(PractitionerRoleEnum.DOCTOR);
		doc.setText(narrative);
		
		//upload the resource
		MethodOutcome  outcome = client
				.create()
				.resource(doc)
				.prettyPrint()
				.encodedXml()
				.execute();
		
		IdDt id = outcome.getId();
        String elementSpecificId = id.getBaseUrl();
        String idPart = id.getIdPart();
        IdDt idNonVersioned = new IdDt(elementSpecificId+"/"+id.getResourceType()+"/"+idPart);

		doctorIds.add(idNonVersioned);
		
		return idNonVersioned;
	}
	
	public List<Practitioner> getAllDoctors()
	{
    	List<Practitioner> doctors = new ArrayList<Practitioner>();
    	
    	for(IdDt doctorID : doctorIds)
    	{    		
    		Practitioner nurse = client.read(Practitioner.class, doctorID);
    		doctors.add(nurse);
    	}
    	
    	return doctors;
	}
	
	//This method deletes all doctors using delete
	public void removeAllDoctors() {
		
		delete("Laser","Eduard");
		delete("House","Gregory");
		delete("Reid","Elliot");
		delete("Grey","Meredith");
		delete("Dog","Doc");
	}
	
	//This method deletes a resource by a combination of it's family name and given name.
	public void delete(String familyName, String givenName) {
		
		Bundle response = 	client
							.search()
							.forResource(Practitioner.class)
							.where(Practitioner.GIVEN.matches().value(givenName))
							.and(Practitioner.FAMILY.matches().value(familyName))
							.execute();
		
		if(!response.isEmpty())
		{
			IdDt id = response.getEntries().get(0).getId();
			
			client
			.delete()
			.resourceById(id)
			.execute();
		}	
	}
}