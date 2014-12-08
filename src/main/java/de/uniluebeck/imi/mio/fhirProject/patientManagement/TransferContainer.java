package patientManagement;

import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;

public class TransferContainer {
	
	public ResourceReferenceDt targetOrganizationReference;
	public long duration;
	public String diagnosisICD;
	public String diagnosisDescription;

}
