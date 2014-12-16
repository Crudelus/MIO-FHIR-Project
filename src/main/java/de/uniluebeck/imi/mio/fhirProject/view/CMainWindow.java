package de.uniluebeck.imi.mio.fhirProject.view;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;


public class CMainWindow extends JFrame 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8756550855388847778L;

	public CMainWindow(String name)
    {
		super(name);
		initComponents();
		addComponents();
		
		//After this comment, please, do NOTHING!! Until the method is finished!
		this.setVisible(true);
	}
	
	private void initComponents()
	{
		this.centerPanel = new JPanel();
		this.patientPanel = new JPanel();
		this.encounterPanel = new JPanel();
		this.observationPanel = new JPanel();
		this.devicePanel = new JPanel();
		this.reportPanel = new JPanel();
		this.centerPanel_TabbedPane = new JTabbedPane();
		this.addMenu();
		
	}
	
	private void addComponents()
	{
		
	}
	
	private void addMenu()
	{
		this.setJMenuBar(CMenu.getMenuBar());
	}
	
	private void createTabbedPane()
	{
		this.centerPanel_TabbedPane.addTab("Patients", this.patientPanel);
		
		this.centerPanel_TabbedPane.addTab("Encounter", this.encounterPanel);
		
		this.centerPanel_TabbedPane.addTab("Observation", this.observationPanel);
		
		this.centerPanel_TabbedPane.addTab("Device", this.devicePanel);
		
		this.centerPanel_TabbedPane.addTab("Report", this.reportPanel);
	}
	
	private void createPatientPanel()
	{
		this.patientPanel.setLayout(new GridLayout(1,1));
		this.patientPanel.add(this.allPatientsTable);
	}
	
	private void createEncounterPanel()
	{
		this.encounterPanel.setLayout(new GridLayout(1,1));
		this.encounterPanel.add(this.allEncounterTable);
	}
	
	private void createObservationPanel()
	{
		this.observationPanel.setLayout(new GridLayout(1,1));
		this.observationPanel.add(this.allObservationTable);
	}
	
	private void createDevicePanel()
	{
		this.devicePanel.setLayout(new GridLayout(1,1));
		this.devicePanel.add(this.allDevicesTable);
	}
	
	private void createReportPanel()
	{
		this.reportPanel.setLayout(new GridLayout(1,1));
		this.reportPanel.add(this.allReportTable);
	}
	
	/*
	 * The components of the MainWindow!
	 */
	JPanel centerPanel;
	JPanel patientPanel;
	JPanel encounterPanel;
	JPanel observationPanel;
	JPanel devicePanel;
	JPanel reportPanel;
	JTabbedPane centerPanel_TabbedPane;
	
	//Patients
	JTable allPatientsTable;
	
	//Devices
	JTable allDevicesTable;
	
	//Observation
	JTable allObservationTable;
	
	//Encounter
	JTable allEncounterTable;
	
	//Report
	JTable allReportTable;
	
}
