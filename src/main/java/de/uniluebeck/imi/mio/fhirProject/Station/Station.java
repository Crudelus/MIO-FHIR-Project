package de.uniluebeck.imi.mio.fhirProject.Station;

import java.util.List;

import ca.uhn.fhir.model.primitive.IdDt;

public class Station 
{
	//Members
	private IdDt m_stationID;
	private String m_stationName;
	private IdDt m_stationDoctor;
	private List<IdDt> m_stationNurses;
	private List<IdDt> m_stationPatients;
	private EStationKind m_stationKind;
	
	/*
	 * Construtors!
	 */
	public Station(IdDt m_stationID)
	{
		
	}
	
	public Station(String m_StationName)
	{
		
	}
	
	/*
	 * Setter & Getter Methods
	 */
	public IdDt getM_stationID()
	{
		return m_stationID;
	}
	
	public String getM_stationName()
	{
		return m_stationName;
	}
	
	public void setM_stationName(String m_stationName)
	{
		this.m_stationName = m_stationName;
	}
	
	public IdDt getM_stationDoctor()
	{
		return m_stationDoctor;
	}
	
	public void setM_stationDoctor(IdDt m_stationDoctor)
	{
		this.m_stationDoctor = m_stationDoctor;
	}
	
	public List<IdDt> getM_stationNurses()
	{
		return m_stationNurses;
	}
	
	public void setM_stationNurses(List<IdDt> m_stationNurses)
	{
		this.m_stationNurses = m_stationNurses;
	}
	
	public List<IdDt> getM_stationPatients()
	{
		return m_stationPatients;
	}
	
	public void setM_stationPatients(List<IdDt> m_stationPatients) 
	{
		this.m_stationPatients = m_stationPatients;
	}
	
	public EStationKind getM_stationKind()
	{
		return m_stationKind;
	}
	
	public void setM_stationKind(EStationKind m_stationKind)
	{
		this.m_stationKind = m_stationKind;
	}
	
	
} 
