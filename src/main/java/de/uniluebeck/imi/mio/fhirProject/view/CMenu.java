package de.uniluebeck.imi.mio.fhirProject.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * 
 * @author Loki
 *
 */
public class CMenu extends JMenu
{

	private static final long serialVersionUID = 3088787687046536427L;
	private static final CMenu singleton = new CMenu();
	/*
	 * Here you will find the methods
	 */
	
	private CMenu()
	{
		this.initComponents();
		this.addComponents();
		this.addActionsListener();
	}
	
	private void initComponents()
	{
		this.menubar = new JMenuBar();
		//Report
		this.menu_Report = new JMenuItem("Reports");
		this.menu_Report_addReport = new JMenuItem("Add Report");
		this.menu_Report_deleteReport = new JMenuItem("Delete Report");
		this.menu_Report_editReport = new JMenuItem("Edit Report");
		this.menu_Report_getAllReports = new JMenuItem("All Reports");
		this.menu_Report_searchReport = new JMenuItem("Search Report");
		//Patient
		this.menu_Patient = new JMenuItem("Patient");
		this.menu_Patient_getAllData = new JMenuItem("All Patient");
		this.menu_Patient_addPatient = new JMenuItem("Add Patient");
		this.menu_Patient_editPatient = new JMenuItem("Edit Patient");
		this.menu_Patient_deletePatient = new JMenuItem("Delete Patient");
		this.menu_Patient_searchPatient = new JMenuItem("Search Patient");
		//Location
		this.menu_Location = new JMenuItem("Location");
		this.menu_Location_getAllLocations = new JMenuItem("All Locations");
		this.menu_Location_searchLocations = new JMenuItem("Search Locations");
		//Observation
		this.menu_Observation = new JMenuItem("Observation");
		this.menu_Observation_getAllObservations = new JMenuItem("All Observations");
		this.menu_Observation_createObservation = new JMenuItem("Create Observation");
		this.menu_Observation_editObservation = new JMenuItem("Edit Observation");
		this.menu_Observation_deleteObservation = new JMenuItem("Delete Observation");
		//Encounter
		this.menu_Encounter = new JMenuItem("Encounter");
		this.menu_Encounter_createEncounter = new JMenuItem("Create Encounter");
		this.menu_Encounter_searchEncounter = new JMenuItem("Search Encounter");
		
	}
	
	private void addComponents()
	{
		//Report
		this.menu_Report.add(this.menu_Report_addReport);
		this.menu_Report.add(this.menu_Report_editReport);
		this.menu_Report.add(this.menu_Report_deleteReport);
		this.menu_Report.add(this.menu_Report_searchReport);
		this.menu_Report.add(this.menu_Report_getAllReports);
		//Patient
		this.menu_Patient.add(this.menu_Patient_getAllData);
		this.menu_Patient.add(this.menu_Patient_editPatient);
		this.menu_Patient.add(this.menu_Patient_deletePatient);
		this.menu_Patient.add(this.menu_Patient_getAllData);
		this.menu_Patient.add(this.menu_Patient_searchPatient);
		//Location
		this.menu_Location.add(this.menu_Location_getAllLocations);
		this.menu_Location.add(this.menu_Location_searchLocations);
		//Observation
		this.menu_Observation.add(this.menu_Observation_getAllObservations);
		/*
		 * After that comment, the JMenuItems will be add to the Menubar.
		 */
		this.menubar.add(this.menu_Patient);
		this.menubar.add(this.menu_Report);
		this.menubar.add(this.menu_Location);
		this.menubar.add(this.menu_Observation);
		this.menubar.add(this.menu_Encounter);
	}
	
	private void addActionsListener()
	{
		//Begin Encounter
		
		this.menu_Encounter_createEncounter.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) 
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		this.menu_Encounter_deleteEncounter.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.menu_Encounter_editEncounter.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.menu_Encounter_getAllEncounter.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.menu_Encounter_searchEncounter.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//End Encounter
		//Begin Patient
		
		this.menu_Patient_addPatient.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.menu_Patient_deletePatient.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.menu_Patient_editPatient.addActionListener(new ActionListener() {
			
		
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.menu_Patient_getAllData.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.menu_Patient_searchPatient.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//End Patient
		
		//Begin Location
		
		this.menu_Location_getAllLocations.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		this.menu_Location_searchLocations.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//End Location
		
		//Begin Observation
		
		this.menu_Observation_createObservation.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.menu_Observation_deleteObservation.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.menu_Observation_editObservation.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.menu_Observation_getAllObservations.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.menu_Observation_searchObservation.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//End Observation
		
		//Begin Report
		
		this.menu_Report_addReport.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.menu_Report_deleteReport.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.menu_Report_editReport.addActionListener(new ActionListener() {
			
		
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.menu_Report_getAllReports.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.menu_Report_searchReport.addActionListener(new ActionListener() {
			
		
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public static JMenuBar getMenuBar()
	{
		
		return singleton.menubar;
	}
	/*
	 * After that comment, there you will find the componets of the MenuBar^^^
	 * But Maybe you will also find the one ring! 
	 */
	
	private JMenuBar menubar;
	//Report
	private JMenuItem menu_Report;
	private JMenuItem menu_Report_addReport;
	private JMenuItem menu_Report_searchReport;
	private JMenuItem menu_Report_editReport;
	private JMenuItem menu_Report_deleteReport;
	private JMenuItem menu_Report_getAllReports;
	//Patient
	private JMenuItem menu_Patient;
	private JMenuItem menu_Patient_getAllData;
	private JMenuItem menu_Patient_searchPatient;
	private JMenuItem menu_Patient_addPatient;
	private JMenuItem menu_Patient_deletePatient;
	private JMenuItem menu_Patient_editPatient;
	//Location
	private JMenuItem menu_Location;
	private JMenuItem menu_Location_getAllLocations;
	private JMenuItem menu_Location_searchLocations;
	//Observation
	private JMenuItem menu_Observation;
	private JMenuItem menu_Observation_createObservation;
	private JMenuItem menu_Observation_editObservation;
	private JMenuItem menu_Observation_deleteObservation;
	private JMenuItem menu_Observation_getAllObservations;
	private JMenuItem menu_Observation_searchObservation;
	//Encounter
	private JMenuItem menu_Encounter;
	private JMenuItem menu_Encounter_createEncounter;
	private JMenuItem menu_Encounter_editEncounter;
	private JMenuItem menu_Encounter_deleteEncounter;
	private JMenuItem menu_Encounter_getAllEncounter;
	private JMenuItem menu_Encounter_searchEncounter;
	
}
