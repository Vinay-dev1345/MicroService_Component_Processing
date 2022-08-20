package com.example.microservice.componentProcessor.service.impl;

import java.io.StringReader;
import java.text.*;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.microservice.componentProcessor.ComponentProcessorApplication;
import com.example.microservice.componentProcessor.Proxy.AuthClient;
import com.example.microservice.componentProcessor.entity.ComponentOrderProcessorEntity;
import com.example.microservice.componentProcessor.repository.ComponentProcessorRepository;
import com.example.microservice.componentProcessor.service.dao.ComponentOrderProcessor;

@Service
public class ComponentOrderProcessorService implements ComponentOrderProcessor {
	
	private static Logger logger = LoggerFactory.getLogger(ComponentProcessorApplication.class);
	public static final String JWTAUTORISATIONENDPOINT = "http://localhost:9001/v1/authorize/user/";
	
	@Autowired
	ComponentProcessorRepository componentProcessorRepository;
	
	@Autowired
	AuthClient authClient;
	
	@Override
	public Map<String, Object> verifyJWTToken(String token) {
		Map<String , Object> jwtResponse = new HashMap<String , Object>();
		try {
			
			logger.info("Request Initiated to get token Validity for Order Creation.");
			String response = authClient.getTokenValidity(token);
			
			logger.info("Response Obtained : "+response);
			JsonReader jsr = Json.createReader(new StringReader(response));
			JsonObject jso = jsr.readObject();
			jsr.close();
			
			jwtResponse.put("isTokenValid" , jso.getBoolean("isValid"));
			jwtResponse.put("errors", false);
		}catch(Exception e) {
			
			logger.warn(e.toString());
			jwtResponse.put("errors", true);
			jwtResponse.put("errorMsg", e.toString());
		}
		
		logger.info("Authorisation Info is sent successfully");
		return jwtResponse;
	}
	
	@Override
	public Map<String , Object> createReturnOrder(JsonObject requestBody) {
		ComponentOrderProcessorEntity order = new ComponentOrderProcessorEntity();
		Map<String , Object> orderResponse = new HashMap<String , Object>();
		
		order.setId(requestBody.getString("orderId"));
	    order.setDelDate(requestBody.getString("dateOfDelivery"));
	    //update order created date 
	    //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    Date date = new Date();
	    String currentDateTime = date.toString();
		order.setCreatedAt(currentDateTime);
		
	    //Customer details mapping
	    String customerName = requestBody.getJsonObject("customerDetails").getString("customerName");
	    String contactNumber = requestBody.getJsonObject("customerDetails").getString("contactNumber");
	    order.setCustContactDetails(contactNumber);
	    order.setCustName(customerName);
	    
	    //Component details Mapping
	    String componentType = requestBody.getJsonObject("componentDetails").getString("componentType");
	    String extraDetails = requestBody.getJsonObject("componentDetails").getString("extraDetails");
	    String componentName = requestBody.getJsonObject("componentDetails").getString("componentName");
	    int qty = requestBody.getJsonObject("componentDetails").getJsonNumber("qty").intValue();
	    
	    order.setCompName(componentName);
	    order.setCompType(componentType);
	    order.setDetails(extraDetails);
	    order.setQty(qty);
	    
	    //Cost Details Mapping
	    double packagingAndDeliveryCost = requestBody.getJsonObject("costingDetails").getJsonNumber("packagingAndDeliveryCost").doubleValue();
	    double processingCost = requestBody.getJsonObject("costingDetails").getJsonNumber("processingCost").doubleValue();
	   order.setPackAndDelCharge(packagingAndDeliveryCost);
	   order.setProcessCharge(processingCost);
	   
	   logger.info("Saving Order Data to Database");
	   //Call to database to save data 
	   componentProcessorRepository.save(order);
	   
	   //prepare success response to be sent to client
	   orderResponse.put("orderId", requestBody.getString("orderId"));
	   orderResponse.put("isSavedSuccessfully", true);
	   orderResponse.put("dateOfDelivery", requestBody.getString("dateOfDelivery"));
	   orderResponse.put("totalCost", packagingAndDeliveryCost + processingCost);
	   orderResponse.put("errors", false);
	   
	   logger.info("Order Creation Status is sent Successfully");
		return orderResponse;
	}


}
