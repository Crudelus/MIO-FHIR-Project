package de.uniluebeck.imi.mio.fhirProject;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticReportStatusEnum;
import de.uniluebeck.imi.mio.fhirProject.report.CReportManagmentSystem;
import de.uniluebeck.imi.mio.fhirProject.report.DeviceCommunicationSystem;

/**
 * Hello world!
 *
 */
public class App 
{
	public static final FhirContext ctx = new FhirContext();
	public static final CReportManagmentSystem rms = new CReportManagmentSystem();
	public static final DeviceCommunicationSystem dcs = new DeviceCommunicationSystem();
	
    public static void main( String[] args )
    {
    		
      
    }
    
}
