package de.uniluebeck.imi.mio.fhirProject.report;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import de.uniluebeck.imi.mio.fhirProject.interfaces.IReport;

public class CReportManagmentSystem implements IReport
{
	private static String serverAdress = "http://fhirtest.uhn.ca/base";
	
	public List<DiagnosticReport> getAllPatientReports(FhirContext ctx,
			IdDt patient) {
		IGenericClient client = ctx.newRestfulGenericClient(serverAdress);
		Bundle response = client.search().forResource(DiagnosticReport.class).where(DiagnosticReport.SUBJECT.hasId(patient)).execute();
		List<DiagnosticReport> target = new ArrayList<DiagnosticReport>();
		for(int i = 0; i < response.size(); i++)
		{
			BundleEntry tmp = response.getEntries().get(i);
			
		}
		return null;
	}

	public MethodOutcome createNewReport(FhirContext ctx)
	{
		IGenericClient client = ctx.newRestfulGenericClient(serverAdress);
		DiagnosticReport newReport = new DiagnosticReport();
		MethodOutcome outcome = client.create().resource(newReport).execute();
		return outcome;
	}

	public DiagnosticReport getReport(FhirContext ctx, IdDt report)
	{
		IGenericClient client = ctx.newRestfulGenericClient(serverAdress);
		return client.read(DiagnosticReport.class, report);
	}

	public boolean updateReport(FhirContext ctx, IdDt report,
			DiagnosticReport newReport)
	{
		IGenericClient client = ctx.newRestfulGenericClient(serverAdress);
		newReport.setId(report);
		MethodOutcome outcome = client.update().resource(newReport).execute();
		
		if(outcome == null)
		{
			return false;
		}else
		{
			return true;
		}
	}

	public boolean deleteReport(FhirContext ctx, IdDt report)
	{
		OperationOutcome outcome;
		IGenericClient client = ctx.newRestfulGenericClient(serverAdress);
		outcome = client.delete().resourceById(report).execute();
		
		if(outcome == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public DiagnosticReport getVRead(FhirContext ctx, IdDt report)
	{
		
		return null;
	}

	public String getXMLString(FhirContext ctx, IdDt report)
	{
		// TODO Auto-generated method stub
		return null;
	}

	

	
	
	
}
