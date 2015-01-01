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
import ca.uhn.fhir.model.dstu.resource.DiagnosticOrder.Item;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticOrderStatusEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.IGenericClient;

public class CLaboratoryValuesManagementSystem {
	static String serverBase = "http://fhirtest.uhn.ca/base";

	private IGenericClient client;
	
	public DiagnosticOrder newOrder(FhirContext ctx, IdDt patient, IdDt orderer, NarrativeDt text, 
			CodeableConceptDt code, CodeableConceptDt bodysite){
		
		this.client = ctx.newRestfulGenericClient(serverBase);

		//IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		
		DiagnosticOrder order = new DiagnosticOrder();
		order.setId(patient);
		// need to get the information from the view form
		//order.setClinicalNotes();
		order.setOrderer(new ResourceReferenceDt(orderer));		//Edna		
		order.setStatus(DiagnosticOrderStatusEnum.REQUESTED);
		order.setText(text);		//von der App
		order.setSubject(new ResourceReferenceDt(patient));
		//order.setItem(item);		//LOINC?
		order.addItem().setCode(code);
		order.addItem().setBodySite(bodysite);
		
		//create in server
		client.create().resource(order).prettyPrint().encodedJson().execute();
		return order;
	}
	
	/*
	public BoundCodeDt<DiagnosticOrderStatusEnum> checkOrder(FhirContext ctx, IdDt orderId){
		this.client = ctx.newRestfulGenericClient(serverBase);
		BoundCodeDt<DiagnosticOrderStatusEnum> status;
		Bundle response = client.search()
			      .forResource(DiagnosticReport.class)
			      .where(DiagnosticReport.IDENTIFIER.equals(orderId))
			      .execute();
		if(response.isEmpty()){
			
		}
		
		return status;
		
	}
	*/
	
	public DiagnosticOrder checkOrder(FhirContext ctx, IdDt orderId){
		this.client = ctx.newRestfulGenericClient(serverBase);
		DiagnosticOrder order;
		Bundle response = client.search()
				.forResource(DiagnosticOrder.class)
				.where(DiagnosticOrder.IDENTIFIER.exactly().identifier(orderId.getValue()))
				.execute();
		if(!response.isEmpty()){
			order = (DiagnosticOrder) response.getResourceById(orderId);
			return order;
		}
		
		return null;
	}
	
	public DiagnosticReport getResult(FhirContext ctx, IdDt patient, IdDt orderId){
		this.client = ctx.newRestfulGenericClient(serverBase);
		DiagnosticReport report;
		Bundle response = client.search()
			      .forResource(DiagnosticReport.class)
			      .where(DiagnosticReport.SUBJECT.hasId(patient))
			      .and(DiagnosticReport.REQUEST.hasId(orderId))
			      .execute();
		
		// return the DiagnosticReport with reference DiagnositcOrder Id == orderId
		if(!response.isEmpty()){
			
			List<DiagnosticReport> reportList = response.getResources(DiagnosticReport.class);
			
			for(int i=0;i<reportList.size();i++){
				if(reportList.get(i).getRequestDetail() == orderId){
					report = reportList.get(i);
					return report;
				}
			}
		}
		return null;
	}
	
}
