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
import ca.uhn.fhir.model.dstu.resource.DiagnosticOrder.Event;
import ca.uhn.fhir.model.dstu.resource.DiagnosticOrder.Item;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticOrderPriorityEnum;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticOrderStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.NarrativeStatusEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.rest.gclient.StringClientParam;

public class CLaboratoryValuesManagementSystem {

	//static FhirContext ctx;
	static String serverBase = "http://fhirtest.uhn.ca/base";
	private IGenericClient client;
	
	//FUNCTION: create a new DiagnosticOrder
	//INPUT: ctx, patID, ordererID usw. 
	public IdDt newOrder(FhirContext ctx, IdDt patient, IdDt orderer, IdDt encounter, List<Event> events, 
			List<Item> items){
		
		this.client = ctx.newRestfulGenericClient(serverBase);
		
		DiagnosticOrder order = new DiagnosticOrder();
		
		//need to get the information from the view form
		order.addIdentifier();
		order.getSubject().setReference(patient);
		order.getOrderer().setReference(orderer);
		order.setEvent(events);
		order.setItem(items);
		order.getEncounter().setReference(encounter);
		order.setStatus(DiagnosticOrderStatusEnum.REQUESTED);
		//order.addItem().setCode(code); //not sure
		//order.addItem().setBodySite(bodysite);
		//order.setSubject(new ResourceReferenceDt(patient));

		//create in server
		client.create().resource(order).prettyPrint().encodedJson().execute();
	
		return order.getId();
	}
	
	//not sure if we need this... 
	public DiagnosticOrder updateOrder(FhirContext ctx, IdDt orderId, DiagnosticOrder orderChange){
		
		this.client = ctx.newRestfulGenericClient(serverBase);

		//DiagnosticOrder order = client.read(DiagnosticOrder.class, orderId);
		/*Patient patient = client.read(Patient.class, order.getSubject().getReference().getValue());
		
		System.out.println(patient.isEmpty());
		String patientName = patient.getName().get(0).getGivenAsSingleString() + " "+ patient.getName().get(0).getFamilyAsSingleString();
		System.out.println("updating the DiagnosticOrder with ID: '" + order.getId().getIdPart()
				+"' for the patient: "+patientName);
		System.out.println("");*/
		//new values : change 된것 만 바꾸려면?
		
		//update in server
		//if(order.getStatus().getValue().equalsIgnoreCase("requested")){
			client.update().resource(orderChange).execute();
		//	System.out.println("Order ID: " + order.getId().getIdPart() + " Updated...");
		//}else{
		//	System.out.println("This order cannot be updated.... Reason: " + order.getStatus().getValueAsString());
		//}
		
		return orderChange;
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
	
	
	// checkOrder function: gets orderId and patientId as input 
	// and returns the DiagnosticOrder Resource which has the same orderId and patientId
	public DiagnosticOrder checkOrder(FhirContext ctx, IdDt orderId, IdDt patientId){
		this.client = ctx.newRestfulGenericClient(serverBase);
		
		DiagnosticOrder order;
		//search for all DiagnosticOrders on the server with patientId
		Bundle response = client.search()
				.forResource(DiagnosticOrder.class)
				.where(DiagnosticOrder.SUBJECT.hasId(patientId))
				.execute();

		System.out.println(response.size()+" DiagnosticOrders are found for patID " + patientId);
		
		//find the right DiagnosticOrder with orderId
		if(!response.isEmpty()){
			order = (DiagnosticOrder) response.getResourceById(orderId);
			return order;
		}
		
		return null;
	}
	
	// getResult Function: gets orderID and patientID as input
	// and returns the DiagnosticResult Resource if it exists. 
	public DiagnosticReport getResult(FhirContext ctx, IdDt orderId, IdDt patient){
		this.client = ctx.newRestfulGenericClient(serverBase);
		DiagnosticOrder order = checkOrder(ctx, orderId, patient);
		
		//if the DiagnosticOrder has the status completed
		if(order.getStatus().getValueAsEnum().equals(DiagnosticOrderStatusEnum.COMPLETED)){
			DiagnosticReport report;
			Bundle response = client.search()
				      .forResource(DiagnosticReport.class)
				      .where(DiagnosticReport.SUBJECT.hasId(patient)) //hasId(orderId))
				      .execute();
			
			System.out.println(response.size() + " Reports are found for patID " + patient.getIdPart());
			
			// return the DiagnosticReport with reference DiagnositcOrder Id == orderId
			if(!response.isEmpty()){
				List<DiagnosticReport> reportList = response.getResources(DiagnosticReport.class);

				for(int i=0;i<reportList.size();i++){
					
					List<ResourceReferenceDt> requests = reportList.get(i).getRequestDetail();
					
					for(int j=0;j<requests.size();j++){
						if(requests.get(j).getReference().getIdPart()==orderId.getIdPart()){
							return reportList.get(i);
						}
					}
				}
			} //end if
		} //end if
		
		// if not return null...
		return null;
	}

	
}
