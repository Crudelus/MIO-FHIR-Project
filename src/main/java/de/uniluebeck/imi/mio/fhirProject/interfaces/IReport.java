package de.uniluebeck.imi.mio.fhirProject.interfaces;

import java.util.List;

import ca.uhn.fhir.model.primitive.IdDt;
import de.uniluebeck.imi.mio.fhirProject.report.CReport;

/**
 * 
 * @author Loki
 * This interface is for the RMS.
 */
public interface IReport
{
	/**
	 * 
	 * @return A representation of the report as an XMLString
	 */
	public String getXMLReport();
	
	/**
	 * 
	 * @return A repesentation of the report as an JSONString
	 */
	public String getJSONReport();
	
	/**
	 * 
	 * @return A list of all reports which are connected with a patient. It is possible that this list is empty.
	 */
	public List<CReport> getAllPatientReports(IdDt patient);
	
	/**
	 * 
	 * @return 
	 */
	public List<CReport> getAllReports();
	
}
