package com.example.microservice.componentProcessor.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.microservice.componentProcessor.service.dao.ComponentProcessor;

@Service
public class AccessoryComponentProcessorService implements ComponentProcessor {

	@Override
	public Map<String, Object> verifyJWTToken(String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String , Object> getComponentProcessingDetails(String componentType , int quantity) {
		return null;
		// TODO Auto-generated method stub

	}

	@Override
	public void createReturnOrder() {
		// TODO Auto-generated method stub

	}

}
