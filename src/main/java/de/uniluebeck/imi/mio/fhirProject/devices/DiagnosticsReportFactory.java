package de.uniluebeck.imi.mio.fhirProject.devices;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Device;
import ca.uhn.fhir.model.dstu.resource.DeviceObservationReport;
import ca.uhn.fhir.model.dstu.resource.DeviceObservationReport.VirtualDeviceChannelMetric;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticReportStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.FHIRDefinedTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.NarrativeStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationReliabilityEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.UnitsOfTimeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;

public class DiagnosticsReportFactory {
	private ServerCommunication communicator;

	public DiagnosticsReportFactory(FhirContext ctx, ServerCommunication comm,
			IdDt hospital) {
		
		communicator = comm;
	}

	public IdDt newLaboratoryReport(IdDt diagOrderId, IdDt patId, IdDt performer) {

		DiagnosticReport lab = new DiagnosticReport();
		lab.setServiceCategory(new CodeableConceptDt(
				"http://hl7.org/fhir/v2/vs/0074", "LAB"));
		lab.addRequestDetail().setReference(diagOrderId);
		lab.setSubject(new ResourceReferenceDt(patId));
		lab.setPerformer(new ResourceReferenceDt(performer));
		lab.setStatus(DiagnosticReportStatusEnum.FINAL);
		
		NarrativeDt ndt = new NarrativeDt();
		ndt.setDiv("Simon ist cool");
		ndt.setStatus(NarrativeStatusEnum.GENERATED);
		lab.setText(ndt);

		// leucocytes
		QuantityDt leuko = new QuantityDt();
		leuko.setSystem("http://unitsofmeasure.org");
		leuko.setUnits("x10*9/L");
		leuko.setCode("x10*9/L");
		leuko.setValue((int) (Math.random() * (10 - 3) + 3));

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
		hemo.setValue((int) (Math.random() * (190 - 90) + 90));

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

	public IdDt newDeviceObservationReport(IdDt deviceId, IdDt diagOrderId, IdDt patId, IdDt performer) {
		
		
		DeviceObservationReport vitPar = new DeviceObservationReport();
		vitPar.setSubject(new ResourceReferenceDt(patId));
		vitPar.setSource(new ResourceReferenceDt(deviceId));
		
		Device dev = communicator.getDevice(deviceId);
		CodeableConceptDt devType = dev.getType();
		String devCode = devType.getCodingFirstRep().getValueAsQueryToken();
		
		
		
		switch(devCode){
		case "Tokometer":
			
			// fetal heart rate
			QuantityDt bpm = new QuantityDt();
			bpm.setSystem("http://unitsofmeasure.org");
			bpm.setUnits("{Beats}/min");
			bpm.setCode("{Beats}/min");
			bpm.setValue((int) (Math.random() * (200 - 100) + 100));

			Observation obvBpm = new Observation();
			List<ResourceReferenceDt> perf = new ArrayList<ResourceReferenceDt>();
			perf.add(0, new ResourceReferenceDt(performer));
			obvBpm.setPerformer(perf);
			obvBpm.setName(new CodeableConceptDt("http://loinc.org", "8867-4")
					.setText("Fetal Heart Rate [Beats per minute]"));

			obvBpm.setStatus(ObservationStatusEnum.FINAL);
			obvBpm.setReliability(ObservationReliabilityEnum.OK);
			obvBpm.setValue(bpm);

			MethodOutcome outBpm = communicator.createRessourceOnServer(obvBpm);
			
			VirtualDeviceChannelMetric heartRate = new VirtualDeviceChannelMetric();
			
			heartRate.setObservation(new ResourceReferenceDt(outBpm.getId()));
			
			ArrayList<VirtualDeviceChannelMetric> metric = new ArrayList<VirtualDeviceChannelMetric>();
			metric.add(heartRate);
			
			vitPar.addVirtualDevice().addChannel().setMetric(metric);

			MethodOutcome outDev1 = communicator.createRessourceOnServer(vitPar);
			 
			return outDev1.getId();
			
		default:
			
			// blood pressure
			QuantityDt pres = new QuantityDt();
			pres.setSystem("http://unitsofmeasure.org");
			pres.setUnits("mm[Hg]");
			pres.setCode("mm[Hg]");
			pres.setValue((int) (Math.random() * (140 - 80) + 80));

			Observation obvPres = new Observation();
			obvPres.setName(new CodeableConceptDt("http://loinc.org", "55284-4")
					.setText("Blood pressure [mass/mercury]"));

			obvPres.setStatus(ObservationStatusEnum.FINAL);
			obvPres.setReliability(ObservationReliabilityEnum.OK);
			obvPres.setValue(pres);

			MethodOutcome outPres = communicator.createRessourceOnServer(obvPres);
			
			VirtualDeviceChannelMetric pressure = new VirtualDeviceChannelMetric();
			
			pressure.setObservation(new ResourceReferenceDt(outPres.getId()));
			
			ArrayList<VirtualDeviceChannelMetric> metric1 = new ArrayList<VirtualDeviceChannelMetric>();
			metric1.add(pressure);
			
			vitPar.addVirtualDevice().addChannel().setMetric(metric1);

			MethodOutcome outDev2 = communicator.createRessourceOnServer(vitPar);
			
			return outDev2.getId();

		}
		
		
	}
}
