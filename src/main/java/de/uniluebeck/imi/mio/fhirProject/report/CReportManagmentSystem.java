package de.uniluebeck.imi.mio.fhirProject.report;

import java.util.List;

import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import de.uniluebeck.imi.mio.fhirProject.interfaces.IReport;

public class CReportManagmentSystem implements IReport
{

	public List<DiagnosticReport> getAllPatientReports(IGenericClient client,
			IdDt patient) {
		// TODO Auto-generated method stub
		return null;
	}

	public MethodOutcome createNewReport(IGenericClient client) {
		// TODO Auto-generated method stub
		return null;
	}

	public DiagnosticReport getReport(IGenericClient client, IdDt report) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean updateReport(IGenericClient client, IdDt report,
			DiagnosticReport newReport) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteReport(IGenericClient client, IdDt report) {
		// TODO Auto-generated method stub
		return false;
	}

	public DiagnosticReport getVRead(IGenericClient client, IdDt report) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getXMLString(IGenericClient client, IdDt report) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
}
