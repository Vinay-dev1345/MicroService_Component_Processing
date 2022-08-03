package com.example.microservice.componentProcessor.controller;


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

import com.example.microservice.componentProcessor.service.ComponentProcessorServices;

@RestController
@RequestMapping("v1/componentproceessor")
public class ComponentProcessorController {
	
	@Autowired
	ComponentProcessorServices componentProcessorServices;
	
	
	@GetMapping("/getdetails")
	public ResponseEntity<?> getProcessingDetails(@RequestParam("type") String productType , @RequestParam("quantity") int qty , @CookieValue("token") String jwtToken ){
		
		Map<String , Object> isTokenValid = componentProcessorServices.verifyJWTToken(jwtToken);
		if((boolean)isTokenValid.get("errors") != false && (boolean)isTokenValid.get("isTokenValid") == true) {
			//call for next function TBD
		}
		System.out.println("validity" + isTokenValid);
		return null;
	}
	
	@PostMapping("/createreturnOrder")
	public ResponseEntity<?> initiateReturnOrder(@RequestBody String order, @CookieValue("token") String jwtToken){
		
		return null;
	}

}
