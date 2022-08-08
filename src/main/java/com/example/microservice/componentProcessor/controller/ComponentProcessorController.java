package com.example.microservice.componentProcessor.controller;


import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.microservice.componentProcessor.service.impl.AccessoryComponentProcessorService;
import com.example.microservice.componentProcessor.service.impl.ComponentOrderProcessorService;
import com.example.microservice.componentProcessor.service.impl.IntegralComponentProcessorService;

@RestController
@RequestMapping("v1/componentproceessor")
public class ComponentProcessorController {
	
	public static final String INTEGRAL = "Integral";
	public static final String ACCESSORY = "Accessory";
	
	@Autowired
	IntegralComponentProcessorService integralcomponentProcessorServices;
	
	@Autowired
	AccessoryComponentProcessorService accessoryComponentProcessorService;
	
	@Autowired
	ComponentOrderProcessorService componentOrderProcessorService;
	
	@CrossOrigin(origins = "http://localhost:4200" , allowCredentials = "true")
	@GetMapping("/getdetails")
	public ResponseEntity<?> getProcessingDetails(@RequestParam("type") String productType , @RequestParam("quantity") int qty , @CookieValue("token") String jwtToken ){
		
		Map<String , Object> response = new HashMap<String , Object>();
		if(productType.equalsIgnoreCase(INTEGRAL)) {
			try {
				Map<String , Object> isTokenValid = integralcomponentProcessorServices.verifyJWTToken(jwtToken);
				System.out.println("controller"+isTokenValid);
				if((boolean)isTokenValid.get("errors") == false && (boolean)isTokenValid.get("isTokenValid") == true) {
					try {
						response = integralcomponentProcessorServices.getComponentProcessingDetails(productType, qty);
						response.put("errors", false);
						System.out.println("order"+response);
					}catch(Exception e) {
						System.out.println(e.toString());
						response.put("errors" , true);
						response.put("errorMessage", "Something went wrong !!!!");
					}
				}else {
					response.put("errors" , true);
					response.put("errorMessage", "Authorisation failed");
				}
			}catch(Exception e) {
				System.out.println(e.toString());
				response.put("errors" , true);
				response.put("errorMessage", " Something went wrong!!!");
			}
		}
		else if(productType.equalsIgnoreCase(ACCESSORY)) {
			try {
				Map<String , Object> isTokenValid =accessoryComponentProcessorService .verifyJWTToken(jwtToken);
				System.out.println("controller"+isTokenValid);
				if((boolean)isTokenValid.get("errors") == false && (boolean)isTokenValid.get("isTokenValid") == true) {
					try {
						response = accessoryComponentProcessorService.getComponentProcessingDetails(productType, qty);
						response.put("errors", false);
						System.out.println(response);
					}catch(Exception e) {
						response.put("errors" , true);
						response.put("errorMessage", "Something went wrong !!!!");
					}
				}else {
					response.put("errors" , true);
					response.put("errorMessage", "Authorisation failed");
				}
			}catch(Exception e) {
				response.put("errors" , true);
				response.put("errorMessage", " Something went wrong!!!");
			}
		}
		else {
			response.put("errors" , true);
			response.put("errorMessage", " Invalid Arguments");
		}

		//System.out.println("validity" + isTokenValid);
		return ResponseEntity.ok(response);
	}
	
	@CrossOrigin(origins = "http://localhost:4200" , allowCredentials = "true")
	@PostMapping("/createreturnorder")
	public ResponseEntity<?> initiateReturnOrder(@RequestBody String order, @CookieValue("token") String jwtToken){
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
						responseBody = componentOrderProcessorService.createReturnOrder(jso);
						responseBody.put("errors", false);
					}catch(Exception e) {
						System.out.println(e.toString());
						responseBody.put("errors" , true);
						responseBody.put("errorMessage", "Something went wrong while recording data !!!!");
					}
				}else {
					responseBody.put("errors" , true);
					responseBody.put("errorMessage", "Authorisation failed");
				}
			}catch(Exception e) {
				responseBody.put("errors" , true);
				responseBody.put("errorMessage", " Error while creating Order");
			}
		}else {
			responseBody.put("errors" , true);
			responseBody.put("errorMessage", "Unable to convert to order due to issues in external systems!!!");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
	}

}
