package de.uniluebeck.imi.mio.fhirProject.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class CEncounter extends JFrame
{
	public CEncounter()
	{
		this.initComponents();
		this.addComponents();
		this.setJMenuBar(CMenu.getMenuBar());
		this.setVisible(true);
	}
	
	private void initComponents()
	{
		this.tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
		this.panel_AddEncounter = new JPanel();
		this.panel_AllEncounter = new JPanel();
		this.panel_DeleteEncounter = new JPanel();
		this.panel_EditEncounter = new JPanel();
		this.panel_SearchEncounter = new JPanel();
		
		this.initPanelAddEncounter();
		this.initPanelAllEncounter();
		this.initPanelDeleteEncounter();
		this.initPanelEditEncounter();
		this.initPanelSearchEncounter();
	}
	
	private void addComponents()
	{
		this.tabs.add("All Encounters", this.panel_AllEncounter);
		this.tabs.add("Search Encounter", this.panel_SearchEncounter);
		this.tabs.add("Add Encounter", this.panel_AddEncounter);
		this.tabs.add("Edit Encounter", this.panel_EditEncounter);
		this.tabs.add("Delete Encounter", this.panel_DeleteEncounter);
	}
	
	private void initPanelAllEncounter()
	{
		this.panel_AllEncounter.setLayout(new BorderLayout());
	}
	
	private void initPanelSearchEncounter()
	{
		this.panel_SearchEncounter.setLayout(new BorderLayout());
	}
	
	private void initPanelAddEncounter()
	{
		this.panel_AddEncounter.setLayout(new BorderLayout());
	}
	
	private void initPanelEditEncounter()
	{
		this.panel_EditEncounter.setLayout(new BorderLayout());
	}
	
	private void initPanelDeleteEncounter()
	{
		this.panel_DeleteEncounter.setLayout(new BorderLayout());
	}
	
	//Components
	private JTabbedPane tabs;
	private JPanel panel_AllEncounter;
	private JPanel panel_SearchEncounter;
	private JPanel panel_AddEncounter;
	private JPanel panel_EditEncounter;
	private JPanel panel_DeleteEncounter;
}
