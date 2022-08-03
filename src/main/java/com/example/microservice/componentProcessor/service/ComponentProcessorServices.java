package com.example.microservice.componentProcessor.service;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

@Service
public class ComponentProcessorServices {

	public static final String JWTAUTORISATIONENDPOINT = "http://localhost:9001/v1/authorize/user/";

	@Autowired
	ComponentProcessorRepository componentProcessorRepository;

	@Autowired
	RestTemplate restTemplate;

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

		return jwtResponse;
	}

	public void getComponentProcessingDetails() {

	}

	public void createReturnOrder() {

	}
}
