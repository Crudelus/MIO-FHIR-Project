package de.uniluebeck.imi.mio.fhirProject.view;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class CPatient extends JFrame
{
	CPatient()
	{
		this.initComponents();
		this.addComponents();
		this.setJMenuBar(CMenu.getMenuBar());
		this.setVisible(true);
	}
	
	private void initComponents()
	{
		this.tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
		this.panel_AddPatient = new JPanel();
		this.panel_AllPatients = new JPanel();
		this.panel_DeletePatient = new JPanel();
		this.panel_EditPatient = new JPanel();
		this.panel_SearchPatient = new JPanel();
		
		this.initializeAllPatients();
		this.initializeAddPatient();
		this.initializeSearchPatient();
		this.initializeDeletePatient();
		this.initializeEditPatient();
	}
	
	private void addComponents()
	{
		tabs.add("Search Patient", this.panel_SearchPatient);
		tabs.add("Add Patient", this.panel_AddPatient);
		tabs.add("Delete Patient", this.panel_DeletePatient);
		tabs.add("Edit Patient", this.panel_EditPatient);
		tabs.add("All Patients", this.panel_AllPatients);
		
	}
	
	private void initializeAllPatients()
	{
		this.panel_AllPatients.setLayout(new BorderLayout());
		this.panel_AllPatients_Table = new JTable();
		
	}
	
	private void initializeSearchPatient()
	{
		this.panel_SearchPatient.setLayout(new BorderLayout());
		this.panel_SearchPatient_Nachname = new JTextField("last name");
		this.panel_SearchPatient_Vorname = new JTextField("given name");
		this.panel_SearchPatient_Search = new JButton("GO!");
		this.panel_SearchPatient.add(this.panel_SearchPatient_Search, BorderLayout.SOUTH);
		JPanel tmp = new JPanel();
		tmp.setLayout(new BoxLayout(tmp, BoxLayout.PAGE_AXIS));
		tmp.add(this.panel_SearchPatient_Nachname);
		tmp.add(this.panel_SearchPatient_Vorname);
		this.panel_SearchPatient.add(tmp, BorderLayout.CENTER);
		
	}
	
	private void initializeAddPatient()
	{
		this.panel_AddPatient.setLayout(new BorderLayout());
	}
	
	private void initializeDeletePatient()
	{
		this.panel_DeletePatient.setLayout(new BorderLayout());
	}
	
	private void initializeEditPatient()
	{
		this.panel_EditPatient.setLayout(new BorderLayout());
	}
	
	
	//Components
	private JTabbedPane tabs;
	private JPanel panel_AllPatients;
	private JPanel panel_SearchPatient;
	private JPanel panel_AddPatient;
	private JPanel panel_DeletePatient;
	private JPanel panel_EditPatient;
	
	private JTable panel_AllPatients_Table;
	
	private JButton panel_SearchPatient_Search;
	private JTextField panel_SearchPatient_Vorname;
	private JTextField panel_SearchPatient_Nachname;
}
