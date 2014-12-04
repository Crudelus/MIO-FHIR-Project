package de.uniluebeck.imi.mio.fhirProject.view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;

public class CShowDiagnosticReport extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6033316555995775826L;
	
	/*
	 * The variables for the view. 
	 */
	private DiagnosticReport report;
	private JPanel mainPanel;
	private JTextArea diagnosticReport_diagnose;
	private JLabel medicName;
	private JLabel patientName;
	private JLabel creationDate;
	private boolean changed;
	
	/*
	 * The methods
	 */
	
	public CShowDiagnosticReport(DiagnosticReport report)
	{
		this.report = report;
	}
	
}
