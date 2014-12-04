package de.uniluebeck.imi.mio.fhirProject.xmlParser;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;

/**
 * The CDomParser is a Parser for an XML-File, to get the special parts of the file.
 * So it should be possible to parse a DiagnosticReport, so you can show the different parts of the document.
 * It should be also possible to change special parts of the document.
 * @author Loki
 *
 */
public class CDomParser 
{
	private DocumentBuilder builder;
	private Document document;
	
	public CDomParser(DiagnosticReport report)
	{
		try
		{
			this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try 
		{
			this.document = builder.parse(report.toString());
		} catch (SAXException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//If there are no problems, now it should be possible to parse the document.
		document.getDocumentElement().normalize();
	}
	
	/**
	 * 
	 * @param tagname
	 * @return The value of the Node, which is searched.
	 */
	public String getSpecialPart(String tagname)
	{
		String erg = "";
		NodeList nList = this.document.getDocumentElement().getElementsByTagName(tagname);
		Node node = nList.item(0);
		erg = node.getNodeValue();		
		return erg;
	}
}
