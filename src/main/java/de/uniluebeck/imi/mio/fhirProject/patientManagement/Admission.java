package de.uniluebeck.imi.mio.fhirProject.patientManagement;

import ca.uhn.fhir.model.dstu.resource.Composition;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.Encounter.Hospitalization;
import ca.uhn.fhir.model.dstu.resource.Patient;

// Container class returned as result of admitting a patient
public class Admission {

	// Maybe just using references would suffice
    public Encounter encounter;
    public Patient patient;
    public Hospitalization hospitalization;
    //public DiagnosticOrder diagnosticOrder;
    public Composition composition;	
}
