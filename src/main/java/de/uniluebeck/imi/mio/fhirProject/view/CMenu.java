package de.uniluebeck.imi.mio.FhirProject.View;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class CMenu extends JMenu
{

	private static final long serialVersionUID = 3088787687046536427L;
	
	/*
	 * Here you will find the methods
	 */
	
	public CMenu()
	{
		this.initComponents();
		this.addComponents();
	}
	
	private void initComponents()
	{
		this.menubar = new JMenuBar();
		this.menu_File = new JMenuItem("File");
		this.menu_File_addReport = new JMenuItem("Add Report");
		this.menu_File_getAllData = new JMenuItem("Patient Data");
		this.menu_Edit = new JMenuItem("Edit");
	}
	
	private void addComponents()
	{
		this.menu_File.add(menu_File_addReport);
		this.menu_File.add(menu_File_getAllData);
		
		this.menubar.add(menu_File);
		this.menubar.add(menu_Edit);
	}
	
	
	public JMenuBar getMenu()
	{
		return this.menubar;
	}
	/*
	 * After that comment, there you will find the componets of the MenuBar^^^
	 * But Maybe you will also find the one ring! 
	 */
	
	private JMenuBar menubar;
	private JMenuItem menu_File;
	private JMenuItem menu_File_getAllData;
	private JMenuItem menu_File_addReport;
	private JMenuItem menu_Edit;
	
	
}
