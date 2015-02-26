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
	public DateTimeDt birthDate; // format 1732-12-24
	public AdministrativeGenderCodesEnum gender;
	public MaritalStatusCodesEnum maritalStatus;
	
	// Address:
	public String line;
	public String zip;
	public String city;
	
	public PatientCreationParameters(){}
	
	// Construct a new set of parameters by using a copy constructor
	PatientCreationParameters(PatientCreationParameters other) 
	{
		this.firstName = other.firstName;
		this.lastName = other.lastName;
		this.maidenName = other.maidenName;
		this.birthDate = other.birthDate;
		this.gender = other.gender;
		this.maritalStatus = other.maritalStatus;

		// Address:
		this.line = other.line;
		this.zip = other.zip;
		this.city = other.city;		
	}
}