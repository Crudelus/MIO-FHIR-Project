package de.uniluebeck.imi.mio.fhirProject.devices;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.DeviceObservationReport;
import ca.uhn.fhir.model.dstu.resource.DeviceObservationReport.VirtualDeviceChannelMetric;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.valueset.FHIRDefinedTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationReliabilityEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.UnitsOfTimeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;

public class DiagnosticsReportFactory {
	private ServerCommunication communicator;

	public DiagnosticsReportFactory(FhirContext ctx, String serverBase,
			IdDt hospital) {
		hospital = new IdDt("Organization", "4237");
		communicator = new ServerCommunication(ctx, serverBase, hospital);
	}

	public IdDt newLaboratoryReport(IdDt diagOrderId, IdDt patId) {

		DiagnosticReport lab = new DiagnosticReport();
		lab.setServiceCategory(new CodeableConceptDt(
				"http://hl7.org/fhir/v2/vs/0074", "LAB"));
		lab.addRequestDetail().setReference(diagOrderId);
		lab.setSubject(new ResourceReferenceDt(patId));

		// leucocytes
		QuantityDt leuko = new QuantityDt();
		leuko.setSystem("http://unitsofmeasure.org");
		leuko.setUnits("x10*9/L");
		leuko.setCode("x10*9/L");
		leuko.setValue(4.6);

		Observation obvLeu = new Observation();
		obvLeu.setName(new CodeableConceptDt("http://loinc.org", "6690-2")
				.setText("Leucocytes [#/volume] in blood by automated count"));

		obvLeu.setStatus(ObservationStatusEnum.FINAL);
		obvLeu.setReliability(ObservationReliabilityEnum.OK);
		obvLeu.setValue(leuko);

		// hemoglobin
		QuantityDt hemo = new QuantityDt();
		hemo.setSystem("http://unitsofmeasure.org");
		hemo.setUnits("g/L");
		hemo.setCode("g/L");
		hemo.setValue(176);

		Observation obvHem = new Observation();
		obvHem.setName(new CodeableConceptDt("http://loinc.org", "718-7")
				.setText("Hemoglobin [mass/volume] in blood"));

		obvHem.setStatus(ObservationStatusEnum.FINAL);
		obvHem.setReliability(ObservationReliabilityEnum.OK);
		obvHem.setValue(hemo);

		MethodOutcome outHem = communicator.createRessourceOnServer(obvHem);
		MethodOutcome outLeu = communicator.createRessourceOnServer(obvLeu);
		lab.addResult().setReference(outHem.getId());
		lab.addResult().setReference(outLeu.getId());
		MethodOutcome outLab = communicator.createRessourceOnServer(lab);

		return outLab.getId();
	}

	public IdDt newDeviceObservationReport(IdDt deviceId, IdDt diagOrderId, IdDt patId) {

		DeviceObservationReport vitPar = new DeviceObservationReport();
		vitPar.setSubject(new ResourceReferenceDt(patId));
		vitPar.setSource(new ResourceReferenceDt(deviceId));

		// temperature
		QuantityDt temp = new QuantityDt();
		temp.setSystem("http://unitsofmeasure.org");
		temp.setUnits("cel(1 K) ");
		temp.setCode("cel(1 K) ");
		temp.setValue(37.3);

		Observation obvTemp = new Observation();
		obvTemp.setName(new CodeableConceptDt("http://loinc.org", "8310-5")
				.setText("Body temperature [degrees Celcius]"));

		obvTemp.setStatus(ObservationStatusEnum.FINAL);
		obvTemp.setReliability(ObservationReliabilityEnum.OK);
		obvTemp.setValue(temp);

		// blood pressure
		QuantityDt pres = new QuantityDt();
		pres.setSystem("http://unitsofmeasure.org");
		pres.setUnits("mm[Hg]");
		pres.setCode("mm[Hg]");
		pres.setValue(120);

		Observation obvPres = new Observation();
		obvPres.setName(new CodeableConceptDt("http://loinc.org", "55284-4")
				.setText("Blood pressure [mass/mercury]"));

		obvPres.setStatus(ObservationStatusEnum.FINAL);
		obvPres.setReliability(ObservationReliabilityEnum.OK);
		obvPres.setValue(pres);

		MethodOutcome outPres = communicator.createRessourceOnServer(obvPres);
		MethodOutcome outTemp = communicator.createRessourceOnServer(obvTemp);
		
		VirtualDeviceChannelMetric pressure = new VirtualDeviceChannelMetric();
		VirtualDeviceChannelMetric temperature = new VirtualDeviceChannelMetric();
		
		pressure.setObservation(new ResourceReferenceDt(outPres.getId()));
		temperature.setObservation(new ResourceReferenceDt(outTemp.getId()));
		
		ArrayList<VirtualDeviceChannelMetric> metric = new ArrayList<VirtualDeviceChannelMetric>();
		metric.add(temperature);
		metric.add(pressure);
		
		vitPar.addVirtualDevice().addChannel().setMetric(metric);

		MethodOutcome outDev = communicator.createRessourceOnServer(vitPar);

		return outDev.getId();
	}
}
