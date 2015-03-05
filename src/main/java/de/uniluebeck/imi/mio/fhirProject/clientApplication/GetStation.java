package de.uniluebeck.imi.mio.fhirProject.clientApplication;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.IGenericClient;
import de.uniluebeck.imi.mio.fhirProject.App;
import de.uniluebeck.imi.mio.fhirProject.Station.Station;

/**
 * 
 * @author Loki
 *
 */
public class GetStation
{
	/**
	 * 
	 * @return
	 */
	public static List<Organization> getStations()
	{
		IGenericClient client = App.ctx.newRestfulGenericClient(App.serverBase);
		Bundle response = client.search().forResource(Organization.class).execute();
		List<Organization> erg = response.getResources(Organization.class);
		
		return erg;
	}
	
	public static Organization getSpecialStation(IdDt station)
	{
		List<Organization> tmp = getStations();
		
		for(Organization org : tmp)
		{
			if(org.getId().equals(station))
			{
				return org;
			}
		}
		
		return null;
	}
	
	public static List<Patient> getPatientsOnStation(IdDt organization)
	{
		List<Patient> erg = new ArrayList<>();
		IGenericClient client = App.ctx.newRestfulGenericClient(App.serverBase);
		Bundle response = client.search().forResource(Encounter.class).where(Encounter.LOCATION.hasId(organization) ).execute();
		List<Encounter> encList = response.getResources(Encounter.class);
		
		for(int i = 0; i < encList.size(); i++)
		{
			ResourceReferenceDt tmp = encList.get(i).getSubject();
			if(tmp != null)
			{
				erg.add(GetPatient.getSpecialPatient(tmp.getReference()));
			}
		}
	
		
		return erg;
	}
}
