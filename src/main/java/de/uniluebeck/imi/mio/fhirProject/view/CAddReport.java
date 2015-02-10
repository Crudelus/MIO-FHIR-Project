package de.uniluebeck.imi.mio.fhirProject.view;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticReportStatusEnum;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import de.uniluebeck.imi.mio.fhirProject.view.functions.AddReportFunctions;

public class CAddReport extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2450804594755496699L;
	
	
	private void initComponents()
	{
		this.setLayout(new BorderLayout());
		//The Panels
		this.centerPanel = new JPanel();
		this.northPanel = new JPanel();
		this.southPanel = new JPanel();
		this.westPanel = new JPanel();
		this.eastPanel = new JPanel();
		
		//The Buttons
		this.okButton = new JButton("Ok and Upload");
		this.breakButton = new JButton("Stop");
		//Do nothing, after that comment
		this.setVisible(true);
	}
	
	private void initActionListener()
	{
		this.okButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				boolean uploaded = AddReportFunctions.uploadnewReport();
				if(uploaded)
				{
					
				}
				
			}
		});
		
		this.breakButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				windowClose();
				
			}
		});
	}
	
	private void addComponents()
	{
		
	}
	
	private void windowClose()
	{
		this.dispose();
	}
	/*
	 * Die Variables
	 */
	private CodeableConceptDt name;
	private DiagnosticReportStatusEnum status;
	private DateTimeDt issued;
	
	private JPanel centerPanel;
	private JPanel northPanel;
	private JPanel southPanel;
	private JPanel westPanel;
	private JPanel eastPanel;
	
	private JButton okButton;
	private JButton breakButton;
	
}
