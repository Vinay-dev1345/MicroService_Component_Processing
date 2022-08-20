package com.example.microservice.componentProcessor.service.impl;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

import com.example.microservice.componentProcessor.repository.ComponentProcessorRepository;
import com.example.microservice.componentProcessor.service.dao.ComponentProcessor;
import com.example.microservice.componentProcessor.ComponentProcessorApplication;
import com.example.microservice.componentProcessor.Proxy.AuthClient;
import com.example.microservice.componentProcessor.Proxy.CostComputeClient;
@Service
public class AccessoryComponentProcessorService implements ComponentProcessor {
	
	private static Logger logger = LoggerFactory.getLogger(ComponentProcessorApplication.class);
	public static final String JWTAUTORISATIONENDPOINT = "http://localhost:9001/v1/authorize/user/";
	public static final String COSTDETAILSENDPOINT = "http://localhost:9003/v1/packagingAndDelivery/getcost?";
	public static final int DEFAULTPROCESSINGDAYS = 2;
	
	@Autowired
	ComponentProcessorRepository componentProcessorRepository;

//	@Autowired
//	RestTemplate restTemplate;
	
	@Autowired
	AuthClient authClient;
	
	@Autowired
	CostComputeClient costComputeClient;
	
	@Override
	public Map<String, Object> verifyJWTToken(String token) {
		Map<String , Object> jwtResponse = new HashMap<String , Object>();
		try {
			
			logger.info("Request Initiated to get token Validity.");
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
	public Map<String , Object> getComponentProcessingDetails(String componentType , int quantity) {
		Map<String , Object> componentProcessResponse = new HashMap<String , Object>();
		Map<String , Object> componentProcessingCost = new HashMap<String , Object>();
		
		logger.info("Request Initiated to get costing details of Accessory Component.");
		String quan = Integer.toString(quantity);
		String response = costComputeClient.getComponentCost(componentType, quan);
		
		logger.info("Response related to costing is Obtained as : "+response);
		JsonReader jsr = Json.createReader(new StringReader(response));
		JsonObject jso = jsr.readObject();
		jsr.close();
		
		//since adequate data is present, start preparing the payload
		//Request Id generator
		Random ran1 = new Random();
		String requestId = Long.toString(ran1.nextLong(10000000, 99999999));
		
		//Processing duration
	    Calendar c = Calendar.getInstance(); 
	    c.add(Calendar.DATE, DEFAULTPROCESSINGDAYS);
	    
	    //Cost Compute and Map
		double pdCost = jso.getJsonNumber("packagingAndDeliveryCost").doubleValue();
		double repairCost = jso.getJsonNumber("processingCost").doubleValue();
		double totalCost = pdCost + repairCost;
		
		componentProcessingCost.put("processingCost",repairCost);
		componentProcessingCost.put("packagingAndDeliveryCost", pdCost);
		componentProcessingCost.put("totalCost", totalCost);
		
		//Map respective details to response body
		componentProcessResponse.put("errors", false);
		componentProcessResponse.put("requestId", requestId);
		componentProcessResponse.put("cost", componentProcessingCost);
		componentProcessResponse.put("dateOfDelivery", c.getTime().toString());
		
		logger.info("Cost Is Computed and sent as " + componentProcessingCost.toString());
		return componentProcessResponse;
	}


}
