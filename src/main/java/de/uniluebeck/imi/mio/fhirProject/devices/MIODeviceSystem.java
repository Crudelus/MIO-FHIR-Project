/**
 * 
 */
package de.uniluebeck.imi.mio.fhirProject.devices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Device;
import ca.uhn.fhir.model.dstu.resource.DeviceObservationReport;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import de.uniluebeck.imi.mio.fhirProject.patientManagement.IPatientManagementSystem;

/**
 * @author Daniel Rehmann, Simon Baumhof, Katharina Merkel, Andrea Essenwanger
 * 
 */
public class MIODeviceSystem implements IDevice {
	FhirContext ctx;
	private ServerCommunication communicator;
	IPatientManagementSystem patSys;
	DiagnosticsReportFactory dFactory;

	/**
	 * @param serverBase
	 * @param ctx
	 * @param hospital
	 */
	public MIODeviceSystem(String serverBase, FhirContext ctx, IPatientManagementSystem patSys) {
		this.ctx = ctx;
		this.patSys = patSys;
		this.communicator = new ServerCommunication(ctx, serverBase, patSys.getHospitalID());
		this.dFactory = new DiagnosticsReportFactory(ctx, communicator, patSys.getHospitalID());
		/*
		 * Create Basic Infastructure
		 */
		this.createBasicInfrastructure();
	}

	/**
	 * Creates the Tokometer
	 */
	public void createBasicInfrastructure() {
		/*
		 * Create Tokometer; add attributes
		 */
		Device tokometer = new Device();
		tokometer.setUdi("toko-001");
		tokometer.setType(new CodeableConceptDt("MIO-KH-HH", "Tokometer"));
		tokometer.setManufacturer("Draeger");
		tokometer.setModel("TokoMaster 5000");
		tokometer.setOwner(new ResourceReferenceDt(patSys.getBirthStation()));
		tokometer.setLocation(patSys.getNICU().getLocation().get(0));
		
		/*
		 * Create
		 */
		Device herbert = new Device();
		herbert.setUdi("smm-012");
		herbert.setType(new CodeableConceptDt("MIO-KH-HH", "Sphygmomanometer"));
		herbert.setManufacturer("Herbert Medical Group");
		herbert.setModel("Sphygmomanometer H-1337");
		herbert.setOwner(new ResourceReferenceDt(patSys.getIMC()));
		herbert.setLocation(patSys.getIMC().getLocation().get(0));
			
		/*
		 * Create Devices on Server
		 */
		MethodOutcome m = communicator.createRessourceOnServer(tokometer);
		communicator.createRessourceOnServer(herbert);
		IdDt tokoID = m.getId();

		/*
		 * For Testing...
		 */
		//
		// DiagnosticOrder order = new DiagnosticOrder();
		//
		//
		// m = communicator.createRessourceOnServer(order);
		// IdDt orderID = m.getId();
		//
		// DiagnosticsReportFactory drf = new DiagnosticsReportFactory(ctx,
		// communicator, hospital);
		// drf.newDeviceObservationReport(tokoID, orderID, new IdDt("Patient",
		// "8090"));
		//
		// Practitioner pract = new Practitioner();
		//
		// m = communicator.createRessourceOnServer(pract);
		// IdDt practID = m.getId();
		//
		// drf.newLaboratoryReport(orderID, new IdDt("Patient",
		// "8090"),practID);
		//
		// // ResourceReferenceDt bs01 = birthstation.getLocation().get(0);
		// // tokometer.setLocation(bs01); //Get via infrastructure
		// // communicator.createRessourceOnServer(tokometer); //nach diesem
		// Schema alle geräte erstellen
		//
		//
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniluebeck.imi.mio.fhirProject.devices.IDevice#getDevice(ca.uhn.fhir
	 * .model.primitive.IdDt)
	 */
	@Override
	public Device getDevice(IdDt deviceId) {
		Device dev = communicator.getDevice(deviceId);
		return dev;
	}


	public boolean setPatient(IdDt devID, IdDt patID) {
		Device dev = communicator.getDevice(devID);
		dev.setPatient(new ResourceReferenceDt(patID));
		return communicator.updateDevice(dev);

	}

	@Override
	public boolean updateDeviceLocation(IdDt devID, IdDt locID) {
		Device dev = communicator.getDevice(devID);
		dev.setLocation(new ResourceReferenceDt(locID));
		return communicator.updateDevice(dev);
	}

	@Override
	public void delAll() {
		communicator.deleteAll();
	}

	public boolean delDev(String id) {
		IdDt deviceId = new IdDt("Device", id);
		communicator.deleteResourceOnServer(deviceId);
		return true;
	}

	@Override
	public ResourceReferenceDt getDeviceLocation(IdDt devId) {
		Device dev = communicator.getDevice(devId);
		return dev.getLocation();
	}

	@Override
	public ArrayList<DeviceObservationReport> getDeviceObservationReportsForPatient(
			IdDt patId) {

		return communicator.getObservationForPatient(patId);

	}

	@Override
	public ArrayList<DeviceAndTimeForPatient> getDeviceAndTimeForPatient(IdDt patId) {
		/*
		 * Get DeviceObservationReports for Patient
		 */
		ArrayList<DeviceObservationReport> obsRepForPat = communicator.getObservationForPatient(patId);
		
		/*
		 * Create List of DeviceAndTimeForPatient :)
		 */
		ArrayList<DeviceAndTimeForPatient> devicesForPat = new ArrayList<>();
		for (DeviceObservationReport report : obsRepForPat) {
			devicesForPat.add(new DeviceAndTimeForPatient(report.getInstant()
					.getValue(), report.getSource().getReference()));
		}
		return devicesForPat;
	}

	@Override
	public List<Device> getHospitalDevices() {
		List<Device> devices = communicator.getAllDevices();
		return devices;
	}
	
	@Override
	public IdDt newLaboratoryReport(IdDt diagnosticOrderId, IdDt patId, IdDt performerId){
		IdDt labRep = dFactory.newLaboratoryReport(diagnosticOrderId, patId, performerId);
		return labRep;
	}
	
	@Override
	public IdDt newDeviceObservationReport(IdDt diagnosticOrderId, IdDt patId, IdDt deviceId, IdDt performer){
		IdDt devRep = dFactory.newDeviceObservationReport(deviceId, diagnosticOrderId, patId,performer);
		return devRep;
	}
	
}
