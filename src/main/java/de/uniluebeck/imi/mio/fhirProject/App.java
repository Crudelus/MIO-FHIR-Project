package de.uniluebeck.imi.mio.fhirProject;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticReportStatusEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.IGenericClient;
import de.uniluebeck.imi.mio.fhirProject.devices.MIODeviceSystem;
import de.uniluebeck.imi.mio.fhirProject.patientManagement.PatientManagementSystem;
import de.uniluebeck.imi.mio.fhirProject.report.CReportManagmentSystem;
import de.uniluebeck.imi.mio.fhirProject.report.DeviceCommunicationSystem;

/**
 * Hello world!
 *
 */
public class App 
{
	public static final FhirContext ctx = new FhirContext();
	public static final String serverBase = "http://fhirtest.uhn.ca/base";
	public static final CReportManagmentSystem rms = new CReportManagmentSystem();
	public static final DeviceCommunicationSystem dcs = new DeviceCommunicationSystem();
	public static final PatientManagementSystem pms = new PatientManagementSystem(ctx.newRestfulGenericClient(serverBase));
	
	
    public static void main( String[] args )
    {
	    IGenericClient client = ctx.newRestfulGenericClient(serverBase);
    }
    
}
