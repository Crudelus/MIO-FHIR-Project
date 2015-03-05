package de.uniluebeck.imi.mio.fhirProject.request;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.DiagnosticOrder;
import ca.uhn.fhir.model.dstu.resource.Order;
import ca.uhn.fhir.model.dstu.resource.DiagnosticOrder.Event;
import ca.uhn.fhir.model.dstu.resource.DiagnosticOrder.Item;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticOrderStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.IGenericClient;

public class CLaboratoryValuesManagementSystem {
	static String serverBase = "http://fhirtest.uhn.ca/base";
	static FhirContext ctx = new FhirContext();
	private IGenericClient client = ctx.newRestfulGenericClient(serverBase);
	
	public String newOrder(IdDt patient, IdDt orderer, IdDt target, IdDt encounter, IdDt reason){
		
		DateTimeDt date = new DateTimeDt();
		
		List<Event> events = new ArrayList<Event>();
		Event event = new Event();
		event.getActor().setReference(orderer);
		event.setDateTime(date.withCurrentTime());
		event.setStatus(DiagnosticOrderStatusEnum.REQUESTED);
		events.add(event);
		
		//set values for diagnosticOrder: only for documentation 
		DiagnosticOrder diag_order = new DiagnosticOrder();
		//need to get the information from the view form
		diag_order.setSubject(new ResourceReferenceDt().setReference(patient));
		diag_order.setOrderer(new ResourceReferenceDt().setReference(orderer));
		diag_order.setEvent(events);
		diag_order.setEncounter(new ResourceReferenceDt().setReference(encounter));
		diag_order.setStatus(DiagnosticOrderStatusEnum.REQUESTED);
		
		//set values for Order: actual order
		Order order = new Order();
		order.addDetail().setResource(diag_order);
		order.setSource(new ResourceReferenceDt().setReference(orderer));
		order.setSubject(new ResourceReferenceDt().setReference(patient));
		order.setTarget(new ResourceReferenceDt().setReference(target));
		order.setDate(date.withCurrentTime());
		order.setReason(reason);
		
		//create in server
		client.create().resource(order).prettyPrint().encodedJson().execute();
		client.create().resource(diag_order).prettyPrint().encodedJson().execute();
		String result = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(order);
		return result;
	}
	
}
