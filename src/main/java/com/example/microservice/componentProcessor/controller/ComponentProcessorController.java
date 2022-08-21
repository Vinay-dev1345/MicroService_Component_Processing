package com.example.microservice.componentProcessor.controller;


import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.microservice.componentProcessor.ComponentProcessorApplication;
import com.example.microservice.componentProcessor.service.impl.AccessoryComponentProcessorService;
import com.example.microservice.componentProcessor.service.impl.ComponentOrderProcessorService;
import com.example.microservice.componentProcessor.service.impl.IntegralComponentProcessorService;

@RestController
@RequestMapping("v1/componentproceessor")
public class ComponentProcessorController {
	private static Logger logger = LoggerFactory.getLogger(ComponentProcessorApplication.class);
	
	public static final String INTEGRAL = "Integral";
	public static final String ACCESSORY = "Accessory";
	
	@Autowired
	IntegralComponentProcessorService integralcomponentProcessorServices;
	
	@Autowired
	AccessoryComponentProcessorService accessoryComponentProcessorService;
	
	@Autowired
	ComponentOrderProcessorService componentOrderProcessorService;
	
	@CrossOrigin(origins = "http://localhost:4200" , allowCredentials = "true")
	@GetMapping(value="/getdetails" , produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> getProcessingDetails(@RequestParam("type") String productType , @RequestParam("quantity") int qty , @RequestHeader(value = "Authorization") String jwtToken ){
		
		Map<String , Object> response = new HashMap<String , Object>();
		if(productType.equalsIgnoreCase(INTEGRAL)) {
			try {
				Map<String , Object> isTokenValid = integralcomponentProcessorServices.verifyJWTToken(jwtToken);
				System.out.println("controller"+isTokenValid);
				if((boolean)isTokenValid.get("errors") == false && (boolean)isTokenValid.get("isTokenValid") == true) {
					try {
						
						logger.info("User is Authorised Successfully");
						response = integralcomponentProcessorServices.getComponentProcessingDetails(productType, qty);
						response.put("errors", false);
						logger.info("Costing Details is as follows : "+response);
						
					}catch(Exception e) {
						
						logger.warn(e.toString());
						response.put("errors" , true);
						response.put("errorMessage", "Something went wrong !!!!");
						
					}
				}else {
					
					logger.info("User is Not Authorised Successfully !!!");
					response.put("errors" , true);
					response.put("errorMessage", "Authorisation failed");
				}
			}catch(Exception e) {
				
				logger.warn(e.toString());
				response.put("errors" , true);
				response.put("errorMessage", " Something went wrong!!!");
			}
		}
		else if(productType.equalsIgnoreCase(ACCESSORY)) {
			try {
				Map<String , Object> isTokenValid =accessoryComponentProcessorService.verifyJWTToken(jwtToken);
				System.out.println("controller"+isTokenValid);
				if((boolean)isTokenValid.get("errors") == false && (boolean)isTokenValid.get("isTokenValid") == true) {
					try {
						
						logger.info("User is Authorised Successfully");
						response = accessoryComponentProcessorService.getComponentProcessingDetails(productType, qty);
						response.put("errors", false);
						logger.info("Costing Details is as follows : "+response);
						
					}catch(Exception e) {
						
						logger.warn(e.toString());
						response.put("errors" , true);
						response.put("errorMessage", "Something went wrong !!!!");
					}
				}else {
					
					logger.info("User is Not Authorised Successfully !!!");
					response.put("errors" , true);
					response.put("errorMessage", "Authorisation failed");
				}
			}catch(Exception e) {
				
				logger.warn(e.toString());
				response.put("errors" , true);
				response.put("errorMessage", " Something went wrong!!!");
			}
		}
		else {
			
			logger.warn("Invalid Arguments has been passed to the server..");
			response.put("errors" , true);
			response.put("errorMessage", " Invalid Arguments");
		}

		logger.info("Response is sent back to the client");
		return ResponseEntity.ok(response);
	}
	
	@CrossOrigin(origins = "http://localhost:4200" , allowCredentials = "true")
	@PostMapping(value="/createreturnorder" , produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> initiateReturnOrder(@RequestBody String order, @RequestHeader(value = "Authorization") String jwtToken){
		Map<String , Object> responseBody = new HashMap<String , Object>();
		
		JsonReader jsr = Json.createReader(new StringReader(order));
		JsonObject jso = jsr.readObject();
		jsr.close();
		
		boolean orderConversion = jso.getBoolean("isConversionToOrder");
		if(orderConversion) {
			try {
				Map<String , Object> isTokenValid = componentOrderProcessorService.verifyJWTToken(jwtToken);
				if((boolean)isTokenValid.get("errors") == false && (boolean)isTokenValid.get("isTokenValid") == true) {
					try {
						
						logger.info("User is Authorised Successfully to Create an Order");
						responseBody = componentOrderProcessorService.createReturnOrder(jso);
						responseBody.put("errors", false);
						
					}catch(Exception e) {
						
						logger.warn(e.toString());
						responseBody.put("errors" , true);
						responseBody.put("errorMessage", "Something went wrong while recording data !!!!");
					}
				}else {
					
					logger.info("User is Not Authorised Successfully to create an order!!!");
					responseBody.put("errors" , true);
					responseBody.put("errorMessage", "Authorisation failed");
				}
			}catch(Exception e) {
				
				logger.warn(e.toString());
				responseBody.put("errors" , true);
				responseBody.put("errorMessage", " Error while creating Order");
			}
		}else {
			
			logger.info("Authorisation Server is down");
			responseBody.put("errors" , true);
			responseBody.put("errorMessage", "Unable to convert to order due to issues in external systems!!!");
		}
		
		logger.info("Response is sent back to client Application");
		if((boolean)responseBody.get("errors") == false) {
			return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
		}else {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
		}
		
		
	}

}
