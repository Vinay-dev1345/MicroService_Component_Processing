package com.example.microservice.componentProcessor.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.microservice.componentProcessor.service.impl.AccessoryComponentProcessorService;
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
	
	@PostMapping("/createreturnOrder")
	public ResponseEntity<?> initiateReturnOrder(@RequestBody String order, @CookieValue("token") String jwtToken){
		
		return null;
	}

}
