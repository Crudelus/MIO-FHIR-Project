package de.uniluebeck.imi.mio.fhirProject.patientManagement;

import ca.uhn.fhir.model.dstu.resource.Organization;

// Mostly work-in-progress:
public class AdmissionParameters {
    public String condition; // ICD 10 or OPS
    Organization station;
}
