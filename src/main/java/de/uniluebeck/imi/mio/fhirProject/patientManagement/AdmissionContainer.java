package de.uniluebeck.imi.mio.fhirProject.patientManagement;

import ca.uhn.fhir.model.dstu.resource.Composition;
import ca.uhn.fhir.model.dstu.resource.Condition;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.Encounter.Hospitalization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;

// Container class returned as result of admitting a patient
public class AdmissionContainer {

	// Maybe just using references would suffice
    public Encounter visit;
    public Encounter adm;
    public Patient patient; // question: do we need the whole object or is the id suffice?

    public Hospitalization hospitalization;
    public Condition condition;

    public Composition composition;	
}

