package de.uniluebeck.imi.mio.fhirProject.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class CReport extends JFrame
{
	public CReport()
	{
		this.initComponents();
		this.addComponents();
		this.setJMenuBar(CMenu.getMenuBar());
		this.setVisible(true);
	}
	
	private void initComponents()
	{
		this.tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
		this.panel_AddReport = new JPanel();
		this.panel_AllReports = new JPanel();
		this.panel_DeleteReport = new JPanel();
		this.panel_EditReport = new JPanel();
		this.panel_SearchReport = new JPanel();
		
		this.initPanelAddReports();
		this.initPanelAllReports();
		this.initPanelDeleteReport();
		this.initPanelEditReport();
		this.initPanelSearchReport();
	}
	
	private void addComponents()
	{
		tabs.add("All Reports", this.panel_AllReports);
		tabs.add("Search Report", this.panel_SearchReport);
		tabs.add("Add Report", this.panel_AddReport);
		tabs.add("Edit Report", this.panel_EditReport);
		tabs.add("Delete Report", this.panel_DeleteReport);
	}
	
	private void initPanelAllReports()
	{
		this.panel_AllReports.setLayout(new BorderLayout());
	}
	
	private void initPanelAddReports()
	{
		this.panel_AddReport.setLayout(new BorderLayout());
	}
	
	private void initPanelDeleteReport()
	{
		this.panel_DeleteReport.setLayout(new BorderLayout());
	}
	
	private void initPanelEditReport()
	{
		this.panel_EditReport.setLayout(new BorderLayout());
	}
	
	private void initPanelSearchReport()
	{
		this.panel_SearchReport.setLayout(new BorderLayout());
	}

	//Components
	private JTabbedPane tabs;
	private JPanel panel_AllReports;
	private JPanel panel_SearchReport;
	private JPanel panel_AddReport;
	private JPanel panel_EditReport;
	private JPanel panel_DeleteReport;
}
