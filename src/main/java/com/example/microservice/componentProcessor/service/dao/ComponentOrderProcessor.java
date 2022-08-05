package com.example.microservice.componentProcessor.service.dao;

import java.util.Map;

public interface ComponentOrderProcessor {
	public void createReturnOrder();
	public Map<String, Object> verifyJWTToken(String token);
}
