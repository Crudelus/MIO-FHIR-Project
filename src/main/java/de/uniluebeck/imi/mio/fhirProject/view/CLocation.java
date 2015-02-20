package de.uniluebeck.imi.mio.fhirProject.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class CLocation extends JFrame
{
	public CLocation()
	{
		this.initComponents();
		this.addComponents();
		this.setJMenuBar(CMenu.getMenuBar());
		this.setVisible(true);
	}
	
	private void initComponents()
	{
		this.tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
		this.panel_AllLocations = new JPanel();
		this.panel_SearchLocation = new JPanel();
		
		this.initPanelAllLocations();
		this.initPanelSearchLocation();
	}
	
	private void addComponents()
	{
		this.tabs.add("All Locations", this.panel_AllLocations);
		this.tabs.add("Search Location", this.panel_SearchLocation);
	}
	
	private void initPanelAllLocations()
	{
		this.panel_AllLocations.setLayout(new BorderLayout());
	}
	
	private void initPanelSearchLocation()
	{
		this.panel_SearchLocation.setLayout(new BorderLayout());
	}
	
	//Components
	private JTabbedPane tabs;
	private JPanel panel_AllLocations;
	private JPanel panel_SearchLocation;
}
