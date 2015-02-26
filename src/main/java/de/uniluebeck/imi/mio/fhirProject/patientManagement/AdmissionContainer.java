package de.uniluebeck.imi.mio.fhirProject.patientManagement;

import ca.uhn.fhir.model.dstu.resource.Composition;
import ca.uhn.fhir.model.dstu.resource.Condition;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.Encounter.Hospitalization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;

/** 
 * Container class returned as result of admitting a patient
 *
 */
public class AdmissionContainer {

    public Encounter visit;
    public Encounter adm;
    public Patient patient;

    public Hospitalization hospitalization;
    public Condition condition;

    public Composition composition;	
}

