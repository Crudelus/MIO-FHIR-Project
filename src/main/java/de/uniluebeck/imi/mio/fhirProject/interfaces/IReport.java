package de.uniluebeck.imi.mio.fhirProject.interfaces;

import java.util.List;

import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;


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
	public List<DiagnosticReport> getAllPatientReports(IdDt patient);
		
	/**
	 * 
	 * @return
	 */
	public MethodOutcome createNewReport();
	
	/**
	 * 
	 * @param report
	 * @return
	 */
	public DiagnosticReport getReport(IdDt report);
	
	/**
	 * 
	 * @param report
	 * @return
	 */
	public boolean updateReport(IdDt report, DiagnosticReport newReport);
	
	/**
	 * 
	 * @param report
	 * @return
	 */
	public boolean deleteReport(IdDt report);
	
	/**
	 * 
	 * @param report
	 * @return
	 */
	public DiagnosticReport getVRead(IdDt report);
	
	/**
	 * 
	 * @param report
	 * @return
	 */
	public String getXMLString(IdDt report);
}
