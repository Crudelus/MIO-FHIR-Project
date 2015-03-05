package de.uniluebeck.imi.mio.fhirProject.clientApplication;

import java.util.List;

import ca.uhn.fhir.model.dstu.resource.Composition;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import de.uniluebeck.imi.mio.fhirProject.App;

public class GetPatient
{
	public static Patient getSpecialPatient(IdDt patientID)
	{
		List<Patient> erg = App.pms.getPatientList();
		
		for(Patient pat : erg)
		{
			if(pat.getId().getValue().compareTo(patientID.getValue()) == 0)
			{
				return pat;
			}
		}
		
		return null;
	}
	
	public static List<Composition> getAllDoctorLettersOfASpecialPatient(IdDt patient)
	{
		return App.obs.getCompositions(patient);
	}
	
	
}
