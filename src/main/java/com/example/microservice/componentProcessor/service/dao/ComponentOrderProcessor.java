package com.example.microservice.componentProcessor.service.dao;

import java.util.Map;

import javax.json.JsonObject;

public interface ComponentOrderProcessor {
	public Map<String , Object> createReturnOrder(JsonObject requestBody);
	public Map<String, Object> verifyJWTToken(String token);
}
