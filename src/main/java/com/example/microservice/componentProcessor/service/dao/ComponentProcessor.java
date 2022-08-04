package com.example.microservice.componentProcessor.service.dao;

import java.util.Map;

public interface ComponentProcessor {
	public Map<String, Object> verifyJWTToken(String token);
	public Map<String , Object> getComponentProcessingDetails(String componentType , int quantity);
	public void createReturnOrder();
}
