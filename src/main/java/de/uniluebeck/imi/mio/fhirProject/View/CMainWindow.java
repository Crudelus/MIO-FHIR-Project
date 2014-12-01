package de.uniluebeck.imi.mio.fhirProject.View;

import javax.swing.JFrame;
import javax.swing.JPanel;
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
	}
	
	private void initComponents()
	{
		this.centerPanel = new JPanel();
		this.allPatientsTable = new JTable();
		
	}
	
	private void addComponents()
	{
		
	}
	
	/*
	 * The components of the MainWindow!
	 */
	JPanel centerPanel;
	JTable allPatientsTable;

	
}
