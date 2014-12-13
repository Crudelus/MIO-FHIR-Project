package patientManagement;

import ca.uhn.fhir.model.dstu.resource.Composition;
import ca.uhn.fhir.model.dstu.resource.Condition;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.Encounter.Hospitalization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;

// Container class returned as result of admitting a patient
public class Admission {

	// Maybe just using references would suffice
    public Encounter visit;
    public Encounter adm;
    public Patient patient; // question: do we need the whole object or is the id suffice?
    public IdDt patientID; // if there is no patient object
    public Hospitalization hospitalization;
    public Condition condition;
    //public DiagnosticOrder diagnosticOrder;
    public Composition composition;	
}

