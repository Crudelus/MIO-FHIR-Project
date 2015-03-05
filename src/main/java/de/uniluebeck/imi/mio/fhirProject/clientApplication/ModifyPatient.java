package de.uniluebeck.imi.mio.fhirProject.clientApplication;

import java.util.List;

import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import de.uniluebeck.imi.mio.fhirProject.App;

public class ModifyPatient
{
	/**
	 * 
	 * @param patientId
	 * @param givenName
	 * @return
	 */
	public static boolean modifyPatientGivenName(IdDt patientId, String givenName)
	{
		try
		{
			Patient pat = GetPatient.getSpecialPatient(patientId);
			pat.addName().addFamily().setValue(givenName);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	/**
	 * 
	 * @param patientId
	 * @param firstName
	 * @return
	 */
	public static boolean modifyPatientFirstName(IdDt patientId, String firstName)
	{
		try
		{
			Patient pat = GetPatient.getSpecialPatient(patientId);
			pat.addName().addGiven().setValue(firstName);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
		
	}
	
	/**
	 * 
	 * @param patientId
	 * @param title
	 * @return
	 */
	public static boolean modifyPatientTitle(IdDt patientId, String title)
	{
		try
		{
			Patient pat = GetPatient.getSpecialPatient(patientId);
			pat.addName().addPrefix().setValue(title);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	/**
	 * 
	 * @param patientId
	 * @param birthdate
	 * @return
	 */
	public static boolean modifyPatientBirthdate(IdDt patientId, DateTimeDt birthdate)
	{
		try
		{
			Patient pat = GetPatient.getSpecialPatient(patientId);
			pat.setBirthDate(birthdate);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	/**
	 * 
	 * @param patientId
	 * @param adress
	 * @return
	 */
	public static boolean setPatientAdress_Adress(IdDt patientId, AddressDt adress)
	{
		try
		{
			Patient pat = GetPatient.getSpecialPatient(patientId);
			List<AddressDt> tmp = pat.getAddress();
			if(isNotInList(tmp, adress))
			{
				tmp.add(adress);
				return true;
			}
			return false;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public static boolean changeMaritalStatus(IdDt patient, MaritalStatusCodesEnum newState)
	{
		Patient tmp = GetPatient.getSpecialPatient(patient);
		tmp.setMaritalStatus(newState);
		App.pms.updatePatient(tmp);
		return true;
	}
	
	
	
	@SuppressWarnings("deprecation")
	private static boolean isNotInList(List<AddressDt> list, AddressDt adress)
	{
		for(AddressDt in : list)
		{
			if(in.getId().getValue().compareTo(adress.getId().getValue()) == 0)
			{
				return false;
			}
		}
		
		return true;
	}
}
