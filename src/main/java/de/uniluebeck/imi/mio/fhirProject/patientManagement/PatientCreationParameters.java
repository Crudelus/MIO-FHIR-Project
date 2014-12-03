package de.uniluebeck.imi.mio.fhirProject.patientManagement;

import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;

//Container for the parameters used to create patients
public class PatientCreationParameters 
{
	public String firstName; 
	public String lastName; 
	public String maidenName; 
	public DateTimeDt birthDate; // like 1732-12-24
	public AdministrativeGenderCodesEnum gender;
	public MaritalStatusCodesEnum maritalStatus;
	
	// address:
	public String line;
	public String zip;
	public String city;
	

	
}