package de.uniluebeck.imi.mio.fhirProject.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class CObservation extends JFrame
{
	public CObservation()
	{
		this.initComponents();
		this.addComponents();
		this.setJMenuBar(CMenu.getMenuBar());
		this.setVisible(true);
	}
	
	private void initComponents()
	{
		this.tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
		this.panel_AddObservation = new JPanel();
		this.panel_AllObservations = new JPanel();
		this.panel_DeleteObservation = new JPanel();
		this.panel_EditObservation = new JPanel();
		this.panel_SearchObservation = new JPanel();
		
		this.initPanelAllObservations();
		this.initPanelAddObservation();
		this.initPanelDeleteObservation();
		this.initPanelEditObservation();
		this.initPanelSearchObservation();
	}
	
	private void addComponents()
	{
		tabs.add("All Observations", this.panel_AllObservations);
		tabs.add("Search Observation", this.panel_SearchObservation);
		tabs.add("Add Observation", this.panel_AddObservation);
		tabs.add("Edit Observation", this.panel_EditObservation);
		tabs.add("Delete Observation", this.panel_DeleteObservation);
	}
	
	private void initPanelAllObservations()
	{
		this.panel_AllObservations.setLayout(new BorderLayout());
	}
	
	private void initPanelSearchObservation()
	{
		this.panel_SearchObservation.setLayout(new BorderLayout());
	}
	
	private void initPanelAddObservation()
	{
		this.panel_AddObservation.setLayout(new BorderLayout());
	}
	
	private void initPanelEditObservation()
	{
		this.panel_EditObservation.setLayout(new BorderLayout());
	}
	
	private void initPanelDeleteObservation()
	{
		this.panel_DeleteObservation.setLayout(new BorderLayout());
	}
	
	//Components
	private JTabbedPane tabs;
	private JPanel panel_AllObservations;
	private JPanel panel_SearchObservation;
	private JPanel panel_AddObservation;
	private JPanel panel_EditObservation;
	private JPanel panel_DeleteObservation;
}
