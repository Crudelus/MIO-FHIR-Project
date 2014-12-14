package de.uniluebeck.imi.mio.fhirProject;


import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Device;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.primitive.IdDt;
import de.uniluebeck.imi.mio.fhirProject.devices.MIODeviceSystem;
import de.uniluebeck.imi.mio.fhirProject.devices.ServerCommunication;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
	FhirContext ctx = new FhirContext(); // TODO should be the same in every
	
	
	MIODeviceSystem mioDev=new MIODeviceSystem("http://fhirtest.uhn.ca/base",ctx,new IdDt("Organization", "6009"));
//	mioDev.createBasicInfrastructure();
	
//	Scanner scan = new Scanner(System.in);
	
	
//	ServerCommunication serv= new ServerCommunication(ctx, "http://fhirtest.uhn.ca/base", new ResourceReferenceDt(new IdDt("Organization", "6009")));
	
//	serv.getAllDevices();
	
	
	
	
	
	
	
//	String wait = scan.next();
//	mioDev.delAll();
    }
}
