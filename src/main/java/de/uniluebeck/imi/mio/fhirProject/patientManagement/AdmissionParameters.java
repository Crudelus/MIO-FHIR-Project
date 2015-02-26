package de.uniluebeck.imi.mio.fhirProject.patientManagement;


import ca.uhn.fhir.model.dstu.valueset.EncounterClassEnum;
import ca.uhn.fhir.model.primitive.IdDt;

/**
 *  List of input parameters for the storage of a new patient admission
 *
 */
public class AdmissionParameters {
    
    public EncounterClassEnum admissionClass;
    public String diagnosisICD; 	// "O80"
    public String diagnosisDescription; // "Entbindung-Spontangeburt"
    public IdDt doctorID;
    public IdDt station;
	public IdDt hospital;
}
