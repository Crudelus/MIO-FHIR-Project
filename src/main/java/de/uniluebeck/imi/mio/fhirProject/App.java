package de.uniluebeck.imi.mio.fhirProject;

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
	mioDev.deleteDevice(new IdDt("Device/daniel"));
    }
}
