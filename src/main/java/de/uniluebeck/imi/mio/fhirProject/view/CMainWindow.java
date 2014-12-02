package de.uniluebeck.imi.mio.fhirProject.view;

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
		
		//After this comment, please, do NOTHING!! Until the method is finished!
		this.setVisible(true);
	}
	
	private void initComponents()
	{
		this.centerPanel = new JPanel();
		this.allPatientsTable = new JTable();
		this.addMenu();
		
	}
	
	private void addComponents()
	{
		
	}
	
	private void addMenu()
	{
		this.setJMenuBar(new CMenu().getMenu());
	}
	
	/*
	 * The components of the MainWindow!
	 */
	JPanel centerPanel;
	JTable allPatientsTable;

	
}
