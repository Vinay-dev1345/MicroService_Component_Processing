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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.microservice.componentProcessor.repository.ComponentProcessorRepository;
import com.example.microservice.componentProcessor.service.dao.ComponentProcessor;

@Service
public class IntegralComponentProcessorService implements ComponentProcessor {

	public static final String JWTAUTORISATIONENDPOINT = "http://localhost:9001/v1/authorize/user/";
	public static final String COSTDETAILSENDPOINT = "http://localhost:9003/v1/packagingAndDelivery/getcost?";
	public static final int DEFAULTPROCESSINGDAYS = 5;
	@Autowired
	ComponentProcessorRepository componentProcessorRepository;

	@Autowired
	RestTemplate restTemplate;
	
	@Override
	public Map<String, Object> verifyJWTToken(String token) {
		Map<String , Object> jwtResponse = new HashMap<String , Object>();
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			HttpEntity<String> entity = new HttpEntity<String>(headers);

			String requestUrl = JWTAUTORISATIONENDPOINT + token;
			String response = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, String.class).getBody();
			
			System.out.println(response);
			JsonReader jsr = Json.createReader(new StringReader(response));
			JsonObject jso = jsr.readObject();
			jsr.close();
			
			jwtResponse.put("isTokenValid" , jso.getBoolean("isValid"));
			jwtResponse.put("errors", false);
		}catch(Exception e) {
			jwtResponse.put("errors", true);
			jwtResponse.put("errorMsg", e.toString());
		}
		System.out.println("jwtRes"+jwtResponse);
		return jwtResponse;
	}
	
	@Override
	public Map<String , Object> getComponentProcessingDetails(String componentType , int quantity) {
		Map<String , Object> componentProcessResponse = new HashMap<String , Object>();
		Map<String , Object> componentProcessingCost = new HashMap<String , Object>();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		String requestUrl = COSTDETAILSENDPOINT + "type=" + componentType + "&count=" + Integer.toString(quantity);
		String response = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, String.class).getBody();
		
		System.out.println(response);
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
		double repairCost = 500.00; //need to be mapped with api response
		double totalCost = pdCost + repairCost;
		
		componentProcessingCost.put("processingCost",repairCost);
		componentProcessingCost.put("packagingAndDeliveryCost", pdCost);
		componentProcessingCost.put("totalCost", totalCost);
		
		//Map respective details to response body
		componentProcessResponse.put("errors", false);
		componentProcessResponse.put("requestId", requestId);
		componentProcessResponse.put("cost", componentProcessingCost);
		componentProcessResponse.put("dateOfDelivery", c.getTime().toString());
		
		return componentProcessResponse;
	}

	@Override
	public void createReturnOrder() {

	}
}
