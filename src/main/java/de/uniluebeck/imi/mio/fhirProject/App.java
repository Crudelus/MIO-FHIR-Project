package de.uniluebeck.imi.mio.fhirProject;

import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticReportStatusEnum;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
       DiagnosticReport test = new DiagnosticReport();
       
       CodeableConceptDt bla = new CodeableConceptDt();
       DiagnosticReportStatusEnum status;
       test.addCodedDiagnosis();
    }
    
}
