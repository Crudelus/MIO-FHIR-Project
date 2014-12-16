package de.uniluebeck.imi.mio.fhirProject.view;
import javax.swing.JFrame;

import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticReportStatusEnum;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;

public class CAddReport extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2450804594755496699L;
	
	private CodeableConceptDt name;
	private DiagnosticReportStatusEnum status;
	private DateTimeDt issued;
	//private Reference  subject;
	//private Reference performer;
}
