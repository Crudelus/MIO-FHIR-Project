package de.uniluebeck.imi.mio.fhirProject.interfaces;

import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;


/**
 * 
 * @author Loki
 * This interface is for the RMS.
 */
public interface IReport
{
	
	/**
	 * 
	 * @return A list of all reports which are connected with a patient. It is possible that this list is empty.
	 */
	public List<DiagnosticReport> getAllPatientReports(FhirContext ctx, IdDt patient);
		
	/**
	 * 
	 * @return
	 */
	public MethodOutcome createNewReport(FhirContext ctx);
	
	/**
	 * 
	 * @param report
	 * @return
	 */
	public DiagnosticReport getReport(FhirContext ctx,IdDt report);
	
	/**
	 * 
	 * @param report
	 * @return
	 */
	public boolean updateReport(FhirContext ctx,IdDt report, DiagnosticReport newReport);
	
	/**
	 * 
	 * @param report
	 * @return
	 */
	public boolean deleteReport(FhirContext ctx,IdDt report);
	
	/**
	 * 
	 * @param report
	 * @return
	 */
	public DiagnosticReport getVRead(FhirContext ctx,IdDt report);
	
	/**
	 * 
	 * @param report
	 * @return A string, which represents the report in XML style.
	 */
	public String getXMLString(FhirContext ctx, IdDt report);
}
