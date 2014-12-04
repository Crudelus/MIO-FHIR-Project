package de.uniluebeck.imi.mio.fhirProject.report;

import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import de.uniluebeck.imi.mio.fhirProject.interfaces.IReport;

public class CReportManagmentSystem implements IReport
{

	public List<DiagnosticReport> getAllPatientReports(FhirContext ctx,
			IdDt patient) {
		// TODO Auto-generated method stub
		return null;
	}

	public MethodOutcome createNewReport(FhirContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	public DiagnosticReport getReport(FhirContext ctx, IdDt report) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean updateReport(FhirContext ctx, IdDt report,
			DiagnosticReport newReport) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteReport(FhirContext ctx, IdDt report) {
		// TODO Auto-generated method stub
		return false;
	}

	public DiagnosticReport getVRead(FhirContext ctx, IdDt report) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getXMLString(FhirContext ctx, IdDt report) {
		// TODO Auto-generated method stub
		return null;
	}

	

	
	
	
}
