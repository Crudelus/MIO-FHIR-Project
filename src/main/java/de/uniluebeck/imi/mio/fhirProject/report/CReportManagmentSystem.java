package de.uniluebeck.imi.mio.fhirProject.report;

import java.util.List;

import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import de.uniluebeck.imi.mio.fhirProject.interfaces.IReport;

public class CReportManagmentSystem implements IReport
{

	public List<DiagnosticReport> getAllPatientReports(IdDt patient) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	public MethodOutcome createNewReport()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public DiagnosticReport getReport(IdDt report)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public boolean updateReport(IdDt report, DiagnosticReport newReport)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteReport(IdDt report)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public DiagnosticReport getVRead(IdDt report)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getXMLString(IdDt report)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
