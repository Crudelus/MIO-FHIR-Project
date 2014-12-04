package de.uniluebeck.imi.mio.fhirProject;

import java.util.Scanner;

import ca.uhn.fhir.model.primitive.IdDt;
import de.uniluebeck.imi.mio.fhirProject.devices.MIODeviceSystem;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
	MIODeviceSystem mioDev=new MIODeviceSystem();
	mioDev.createBasicInfrastructure();
	Scanner scan = new Scanner(System.in);
	String wait = scan.next();
	mioDev.delAll();
//	mioDev.delDev("5950");
    }
}
