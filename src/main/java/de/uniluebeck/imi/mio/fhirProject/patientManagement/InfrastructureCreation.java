package de.uniluebeck.imi.mio.fhirProject.patientManagement;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Composition;

import ca.uhn.fhir.model.dstu.resource.Location;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.valueset.OrganizationTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;



public class InfrastructureCreation {
	
	private IGenericClient client;
	
	private IdentifierDt hospitalIdentifier;
	private IdDt hospitalID;
	
	private IdDt imcID;
	private IdDt birthStationID;
	private IdDt nicuStationID;
	
	public InfrastructureCreation(IGenericClient inClient)
	{
		this.client = inClient;
		
		try{
			createInfrastructure(inClient);
			
		}catch(IOException e){
			
			e.printStackTrace();
			System.out.println("Error during infrastructure creation!");			
		}
		
	}

	public IdentifierDt getHospitalIdentifier()
	{
		return hospitalIdentifier;
	}
	
	public IdDt getHospitalID()
	{
		return hospitalID;
	}
	
	public Organization getBirthStation()
	{
		return client.read(Organization.class, birthStationID);
	}
	
	public Organization getIMC()
	{
		return client.read(Organization.class, imcID);		
	}
	
	public Organization getNICU()
	{
		return client.read(Organization.class, nicuStationID);		
	}
	
	/**
	 * This method creates all necessary infrastructure-resources
	 * 
	 * @param ctx
	 * @param uploadBundle
	 * @param client
	 * @throws IOException
	 */
	public void createInfrastructure(IGenericClient client) throws IOException
	{
		Organization hospital = new Organization();		
		
		/*
		 * Create MIO hospital
		 */
		
		hospitalIdentifier = new IdentifierDt("http://www.kh-hh.de/mio/organizations","MIO");
		
		hospital.addIdentifier().setSystem(hospitalIdentifier.getSystem()).setValue(hospitalIdentifier.getValue());
		hospital.setName("MIO Krankenhaus Hamburg");
		
		
		ResourceReferenceDt hospitalResource = new ResourceReferenceDt();
		hospitalResource.setResource(hospital);
		
		Location hospitalLocation = new Location();
		
		// Create new hospital-address
		AddressDt hospitalAddress = createAddress("Musterstrasse 1", "Hamburg", "Hamburg", "22113", "Germany");
		
		
		/*
		 *  Create reception (REC)
		 */
		Organization reception = new Organization();
		ResourceReferenceDt receptionResource = new ResourceReferenceDt();
		createStation(reception, hospitalResource, "Reception", client, receptionResource);
		
		// Create room in reception
		AddressDt hospitalReceptionRoomAddress = createAddress("Musterstrasse 1, Reception", "Hamburg", 
				"Hamburg", "22113", "Germany");
		hospitalReceptionRoomAddress.addLine("Reception Room");
		
		
		Location receptionRoomLocation= new Location();
		receptionRoomLocation.setAddress(hospitalReceptionRoomAddress);
		receptionRoomLocation.setPartOf(receptionResource);
		receptionRoomLocation.setName("Reception Room");
		receptionRoomLocation.setId(uploadLocation(client, receptionRoomLocation));
				
		createLocation(hospitalLocation, client, hospitalAddress, hospital, receptionRoomLocation);
				
		
		//hospital.setId(uploadOrganization(client, hospital));
		hospitalID = uploadOrganization(client, hospital);
		hospital.setId(hospitalID);
				
		
		/*
		 *  Create birth station (BS)
		 */
		Organization birthStation = new Organization();
		ResourceReferenceDt birthStationResource = new ResourceReferenceDt();
		birthStationID = createStation(birthStation, hospitalResource, "Birth Station", client, birthStationResource);
		
		// Create new birth-station location
		AddressDt birthStationAddress = createAddress("Musterstrasse 1, Abteilung 1", "Hamburg", 
				"Hamburg", "22113", "Germany");
		
		Location birthStationLocation = new Location();
		
		// Create room in birth station
		AddressDt birthStationRoomAddress = createAddress("Musterstrasse 1, Abteilung 1", "Hamburg", 
				"Hamburg", "22113", "Germany");
		birthStationRoomAddress.addLine("BS-01");
		
		Location birthStationRoomLocation = new Location();
		birthStationRoomLocation.setAddress(birthStationRoomAddress);
		birthStationRoomLocation.setPartOf(birthStationResource);
		birthStationRoomLocation.setName("BS-01");
		birthStationRoomLocation.setId(uploadLocation(client, birthStationRoomLocation));
		
		
		createLocation(birthStationLocation, client, birthStationAddress, birthStation, birthStationRoomLocation);
		
		


		/*
		 *  Create neonatal intensive care unit (NICU)
		 */
		Organization nicuStation = new Organization();
		ResourceReferenceDt nicuStationResource = new ResourceReferenceDt();
		nicuStationID = createStation(nicuStation, hospitalResource, "Neonatal Intensive Care Unit", client, nicuStationResource);

		// Create NICU-location
		AddressDt nicuAddress = createAddress("Musterstrasse 1, Abteilung 2", "Hamburg", "Hamburg", "22113", "Germany");
		
		Location nicuLocation = new Location();
		
		// Create room in NICU station
		AddressDt nicuRoomAddress = createAddress("Musterstrasse 1, Abteilung 2", "Hamburg", 
				"Hamburg", "22113", "Germany");
		nicuRoomAddress.addLine("NICU-01");
				
		Location nicuRoomLocation = new Location();
		nicuRoomLocation.setAddress(nicuRoomAddress);
		nicuRoomLocation.setPartOf(nicuStationResource);
		nicuRoomLocation.setId(uploadLocation(client, nicuRoomLocation));
		nicuRoomLocation.setName("NICU-01");
		
		
		createLocation(nicuLocation, client, nicuAddress, nicuStation, nicuRoomLocation);
		
		
		
		
		
		
		/*
		 *  Create intensive care unit (ICU)
		 */
		Organization icuStation = new Organization();
		ResourceReferenceDt icuStationReference = new ResourceReferenceDt();
		createStation(icuStation, hospitalResource, "Intensive Care Unit", client, icuStationReference);

		// Create ICU-location
		AddressDt icuAddress = createAddress("Musterstrasse 1, Abteilung 3", "Hamburg", "Hamburg", "22113", "Germany");
		
		Location icuLocation= new Location();
		
		// Create room in ICU
		AddressDt icuRoomAddress = createAddress("Musterstrasse 1, Abteilung 3", "Hamburg", "Hamburg", "22113", "Germany");
		icuRoomAddress.addLine("ICU-01");
		
		Location icuRoomLocation= new Location();
		icuRoomLocation.setAddress(icuRoomAddress);
		icuRoomLocation.setPartOf(icuStationReference);
		icuRoomLocation.setId(uploadLocation(client, icuRoomLocation));
		icuRoomLocation.setName("ICU-01");
		
		createLocation(icuLocation, client, icuAddress, icuStation, icuRoomLocation);
		
		
		
		
		
		
		
		/*
		 *  Create IMC
		 */
		Organization imcStation = new Organization();
		ResourceReferenceDt imcStationReference = new ResourceReferenceDt();
		imcID = createStation(imcStation, hospitalResource, "Intermediate Care", client, imcStationReference);
		
		// Create IMC-location
		AddressDt imcAddress = createAddress("Musterstrasse 1, Abteilung 4", "Hamburg", "Hamburg", "22113", "Germany");
		
		Location imcLocation= new Location();
				
		// Create room in IMC
		AddressDt imcRoomAddress = createAddress("Musterstrasse 1, Abteilung 4", "Hamburg", "Hamburg", "22113", "Germany");
		imcRoomAddress.addLine("IMC-01");
		
		Location imcRoomLocation= new Location();
		imcRoomLocation.setAddress(imcRoomAddress);
		imcRoomLocation.setPartOf(imcStationReference);
		imcRoomLocation.setId(uploadLocation(client, imcRoomLocation));
		imcRoomLocation.setName("IMC-01");
		
		createLocation(imcLocation, client, imcAddress, imcStation, imcRoomLocation);
		
		
		/*
		 *  Create Laboratory (LAB)
		 */
		Organization laboratoryStation = new Organization();
		ResourceReferenceDt laboratoryStationReference = new ResourceReferenceDt();
		createStation(laboratoryStation, hospitalResource, "Laboratory", client, laboratoryStationReference);

		// Create LAB-location
		AddressDt labAddress = createAddress("Musterstrasse 1, Abteilung 6", "Hamburg", "Hamburg", "22113", "Germany");
		
		// Create room in Lab
		AddressDt labRoomAddress = createAddress("Musterstrasse 1, Abteilung 6", "Hamburg", "Hamburg", "22113", "Germany");
		labRoomAddress.addLine("LAB-01");
		
		Location labRoomLocation= new Location();
		labRoomLocation.setAddress(labRoomAddress);
		labRoomLocation.setPartOf(laboratoryStationReference);
		labRoomLocation.setId(uploadLocation(client, labRoomLocation));
		labRoomLocation.setName("LAB-01");
		
		Location labLocation= new Location();
		createLocation(labLocation, client, labAddress, laboratoryStation, labRoomLocation);
		
		
		
	
		
		
		/*
		System.out.println("Hospital Location:" + hospital.getLocation());	

		System.out.println("BirthStation Location:" + birthStation.getLocation());
		System.out.println("NICU Location:" + nicuStation.getLocation());
		System.out.println("ICU Location:" + icuStation.getLocation());
		System.out.println("IMC Location:" + imcStation.getLocation());
		System.out.println("Lab Location:" + laboratoryStation.getLocation());
		System.out.println("Lab ID: " + laboratoryStation.getId().getValue());
		System.out.println("Hospital ID: " + hospital.getId().getValue());
		*/
	}
	
	
	
	
	/**
	 * This method deletes all infrastructure using the organization's names
	 * @param client
	 */
	public void wipeInfrastructure(IGenericClient client)
	{
		deleteOrganization(client, "MIO Krankenhaus Hamburg");
		deleteOrganization(client, "Birth Station");
		deleteOrganization(client, "Neonatal Intensive Care Unit");
		deleteOrganization(client, "Intensive Care Unit");
		deleteOrganization(client, "Intermediate Care");
		deleteOrganization(client, "Laboratory");
		
		
		deleteLocation(client, "MIO Krankenhaus Hamburg");
		deleteLocation(client, "Birth Station");
		deleteLocation(client, "Neonatal Intensive Care Unit");
		deleteLocation(client, "Intensive Care Unit");
		deleteLocation(client, "Intermediate Care");
		deleteLocation(client, "Laboratory");
		
		
		deleteLocation(client, "Reception Room");
		deleteLocation(client, "BS-01");
		deleteLocation(client, "NICU-01");
		deleteLocation(client, "ICU-01");
		deleteLocation(client, "IMC-01");
		deleteLocation(client, "LAB-01");
		
	}
	
	
	
	
	/**
	 * This method searches an organization on the server using the specified client and deletes it if found
	 * 
	 * @param client
	 * @param organizationName
	 */
	public void deleteOrganization(IGenericClient client, String organizationName)
	{
		
		// Find resource by name and return a bundle containing the resource-information
		Bundle response = client
				.search()
				.forResource(Organization.class)
				.where(Organization.NAME.matches().value(organizationName))
				.execute();

		// If the bundle isn't empty, delete the resource on the server using the resource-ID
		if(!response.isEmpty())
		{
			for(int i= 0; i < response.size(); i++)
			{
				IdDt id = response.getEntries().get(i).getId();
				
				client
				.delete()
				.resourceById(id)
				.execute();
			}			
		}		
	}
	
	
	
	/**
	 * This method searches an organization on the server using the specified client and deletes it if found
	 * 
	 * @param client
	 * @param organizationName
	 */
	public void deleteLocation(IGenericClient client, String locationName)
	{
		
		// Find resource by name and return a bundle containing the resource-information
		Bundle response = client
				.search()
				.forResource(Organization.class)
				.where(Location.NAME.matches().value(locationName))
				.execute();

		// If the bundle isn't empty, delete the resource on the server using the resource-ID
		if(!response.isEmpty())
		{
			for(int i= 0; i < response.size(); i++)
			{
				IdDt id = response.getEntries().get(i).getId();
				
				client
				.delete()
				.resourceById(id)
				.execute();
			}			
		}		
	}
	
	
	/**
	 * This method creates an organization on the server using the specified IGenericClient and 
	 * retrieves the technical ID. The ID is then shortened and stripped off the appended version
	 * 
	 * @param client
	 * @param organization
	 * @return The non-versioned technical ID of the organization
	 */
	public IdDt uploadOrganization(IGenericClient client, Organization organization)
	{
		
		MethodOutcome  outcome = client
							.create()
							.resource(organization)
							.prettyPrint()
							.encodedXml()
							.execute();
		
		IdDt id = outcome.getId();
		String idPart = id.getIdPart();
		String elementSpecificId = id.getBaseUrl();
		IdDt idNonVersioned = new IdDt(elementSpecificId + "/" + id.getResourceType() + "/" + idPart);
		
		// Set ID on local patient object
        organization.setId(idNonVersioned);	
		
		return idNonVersioned;		
	}
	
	
	
	
	/**
	 * This method creates an organization on the server using the specified IGenericClient and 
	 * retrieves the technical ID. The ID is then shortened and stripped off the appended version
	 * 
	 * @param client
	 * @param location
	 * @return The non-versioned technical ID of the location
	 */
	public IdDt uploadLocation(IGenericClient client, Location location)
	{		
		MethodOutcome  outcome = client
				.create()
				.resource(location)
				.prettyPrint()
				.encodedXml()
				.execute();
		
		IdDt id = outcome.getId();
		String idPart = id.getIdPart();
		String elementSpecificId = id.getBaseUrl();
		IdDt idNonVersioned = new IdDt(elementSpecificId + "/" + id.getResourceType() + "/" + idPart);

        // Set ID on local patient object
        location.setId(idNonVersioned);         
	
		return idNonVersioned;		
	}

	/**
	 * This method creates a location for a given organization
	 * @param organizationLocation
	 * @param client
	 * @param address
	 * @param organization
	 */
	IdDt createLocation(
			Location organizationLocation, 
			IGenericClient client, 
			AddressDt address, 
			Organization organization,
			Location organizationRoomLocation)
	{
		// Add created address to the location of the organization
		organizationLocation.setAddress(address);
		
		// Create a reference in order create the Location on the server and retrieve a valid ID
		ResourceReferenceDt organizationLocationReference = new ResourceReferenceDt();
		
		ResourceReferenceDt organizationRoomLocationReference = new ResourceReferenceDt();
		organizationRoomLocationReference.setResource(organizationRoomLocation);
		organizationRoomLocationReference.setReference(organizationRoomLocation.getId());
		
		// Set the created location as base of the resource reference
		organizationLocationReference.setResource(organizationLocation);
		organizationLocationReference.setReference(organizationLocation.getId());
		
		// Create a list of locations and add them to the organization
		List<ResourceReferenceDt> organizationLocationsList = new ArrayList<ResourceReferenceDt>();
		organizationLocationsList.add(organizationLocationReference);
		organizationLocationsList.add(organizationRoomLocationReference);
		
		IdDt result_id = uploadLocation(client, organizationLocation);
		organizationLocation.setId(result_id);

		organization.setLocation(organizationLocationsList);
		
		return result_id;
	}
	
	/**
	 * This method instantiates an AdressDt-object with the given parameters
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 * @return
	 */
	public AddressDt createAddress(
			String street, 
			String city, 
			String state,
			String zip, 
			String country)
	{
		AddressDt address = new AddressDt(); 
		address.addLine(street);
		address.setCity(city);
		address.setState(state);
		address.setZip(zip);
		address.setCountry(country);
		
		return address;				
	}
	
	
	
	/**
	 * This method creates a station from the given parameters
	 * @param organization
	 * @param containingResourceReference
	 * @param stationName
	 * @param client
	 * @param organizationReference
	 */
	public IdDt createStation(
			Organization organization, 
			ResourceReferenceDt containingResourceReference, 
			String stationName, 
			IGenericClient client,
			ResourceReferenceDt organizationReference)
	{		
		organization.setType(OrganizationTypeEnum.HOSPITAL_DEPARTMENT);
		
		// Specify the superior organization e.g. the hospital where the station is located
		organization.setPartOf(containingResourceReference);
		
		// Give the station a name
		organization.setName(stationName);	

		// Upload the station to the server
		IdDt stationId = uploadOrganization(client, organization);
		
		// Create a resource reference
		organizationReference.setReference(organization.getId());
		
		return stationId;
	}	
}
