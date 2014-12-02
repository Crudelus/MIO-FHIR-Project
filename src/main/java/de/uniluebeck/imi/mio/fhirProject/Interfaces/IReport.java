package de.uniluebeck.imi.mio.fhirProject.Interfaces;

import java.util.List;

import de.uniluebeck.imi.mio.fhirProject.report.CReport;

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
	 * @return  
	 */
	public List<CReport> getAllPatientReports();
}
