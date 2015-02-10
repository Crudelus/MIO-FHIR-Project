package de.uniluebeck.imi.mio.fhirProject.view.functions;

import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticReportStatusEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import de.uniluebeck.imi.mio.fhirProject.App;
import de.uniluebeck.imi.mio.fhirProject.report.CReportManagmentSystem;



public class AddReportFunctions
{
	public static boolean uploadnewReport(String author, String date, String title ,String content, int status)
	{
		
		MethodOutcome outcome = App.rms.createNewReport(App.ctx);
		DiagnosticReport report = new DiagnosticReport();
		report.setId(outcome.getId());
		report.setName(new CodeableConceptDt("", title));
		//SetStatus
		switch(status)
		{
			case 1: report.setStatus(DiagnosticReportStatusEnum.AMENDED);break;
			case 2: report.setStatus(DiagnosticReportStatusEnum.APPENDED);break;
			case 3: report.setStatus(DiagnosticReportStatusEnum.CANCELLED);break;
			case 4: report.setStatus(DiagnosticReportStatusEnum.CORRECTED);break;
			case 5: report.setStatus(DiagnosticReportStatusEnum.ENTERED_IN_ERROR);break;
			case 6: report.setStatus(DiagnosticReportStatusEnum.FINAL);break;
			case 7: report.setStatus(DiagnosticReportStatusEnum.PARTIAL);break;
			case 8: report.setStatus(DiagnosticReportStatusEnum.REGISTERED);break;
			default: report.setStatus(DiagnosticReportStatusEnum.FINAL);break;
		}
		
		return true;
	}
}
