package de.uniluebeck.imi.mio.fhirProject.clientApplication;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.primitive.IdDt;
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
		Organization org[] = new Organization[3];
		org[0] = App.pms.getIMC();
		org[1] = App.pms.getNICU();
		
		List<Organization> erg = new ArrayList<Organization>();
		erg.add(org[0]);
		erg.add(org[1]);
		return erg;
	}
}
