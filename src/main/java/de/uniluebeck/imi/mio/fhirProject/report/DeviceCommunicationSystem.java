package de.uniluebeck.imi.mio.fhirProject.report;

import ca.uhn.fhir.model.primitive.IdDt;

public class DeviceCommunicationSystem
{
	/*
	 * Es wird eine DiagnosticOrder an das DMS (Device managment System) geschickt, mit dem entsprtechenden
	 * Loinccode, die IDdt, zu bekommen über den MethodOutcome. Sowie die IDdt des Patienten.
	 * 
	 */
	
	public void createMedicalOrder(IdDt diagnosticOrder, IdDt patient)
	{
		/*
		 * Idee des Vorgehens:
		 * Es werden die IdDt übergeben und alles wird an das DMS übergeben.
		 * 
		 */
		
	}
	
	
	
}
