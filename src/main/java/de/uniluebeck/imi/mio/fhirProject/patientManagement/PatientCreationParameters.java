package de.uniluebeck.imi.mio.fhirProject.patientManagement;

import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.MaritalStatusCodesEnum;

//Container for the parameters used to create patients
public class PatientCreationParameters 
{
	public String firstName; 
	public String lastName; 
	public String maidenName; 
	public String id;
	public String birthDate; 
	public AdministrativeGenderCodesEnum gender;
	public MaritalStatusCodesEnum maritalStatus;	
}
