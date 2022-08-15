package com.example.microservice.componentProcessor;


import static org.mockito.Mockito.*;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.microservice.componentProcessor.repository.ComponentProcessorRepository;
import com.example.microservice.componentProcessor.service.impl.AccessoryComponentProcessorService;
import com.example.microservice.componentProcessor.service.impl.IntegralComponentProcessorService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ComponentProcessorApplicationTests {

	@Mock
	ComponentProcessorRepository componentProcessorRepository;
	
	@Mock
	RestTemplate restTemplate;
	
	@InjectMocks
	AccessoryComponentProcessorService accessoryComponentProcessorService;
	
	@InjectMocks
	IntegralComponentProcessorService integralComponentProcessorService;
	
	@Test
	public void verifyJWTAuthorisationForAccessoryComponentProcessorService() {
		when(this.restTemplate.exchange((String) any(), (org.springframework.http.HttpMethod) any(),
                (org.springframework.http.HttpEntity<Object>) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn(new ResponseEntity<>("{\"isValid\":true}", HttpStatus.OK));
		Map<String, Object> actualResponse = accessoryComponentProcessorService.verifyJWTToken("sampleToken");
		Assertions.assertEquals(true, actualResponse.get("isTokenValid"));
	}
	
	@Test
	public void verifyProcessingDetailsResponseForAccessoryComponentService() {
		String responseBody = "{\"processingCost\":300.0,\"packagingAndDeliveryCost\":200.0,\"errors\":false}";
		when(this.restTemplate.exchange((String) any(), (org.springframework.http.HttpMethod) any(),
                (org.springframework.http.HttpEntity<Object>) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn(new ResponseEntity<>(responseBody, HttpStatus.OK));
		Map<String , Object> actualResponse = accessoryComponentProcessorService.getComponentProcessingDetails("Accessory", 1);
		Map<String, Object> actualTotalCostObtained = (Map<String, Object>) actualResponse.get("cost");
		Assertions.assertEquals(500.00, actualTotalCostObtained.get("totalCost"));
	}
	
	@Test
	public void verifyJWTAuthorisationFailedForAccessoryComponentProcessorService() {
		when(this.restTemplate.exchange((String) any(), (org.springframework.http.HttpMethod) any(),
                (org.springframework.http.HttpEntity<Object>) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn(new ResponseEntity<>("{\"isValid\":false}", HttpStatus.OK));
		Map<String, Object> actualResponse = accessoryComponentProcessorService.verifyJWTToken("sampleToken");
		Assertions.assertEquals(false, actualResponse.get("isTokenValid"));
	}
	
	@Test
	public void verifyJWTAuthorisationForIntegralComponentProcessorService() {
		when(this.restTemplate.exchange((String) any(), (org.springframework.http.HttpMethod) any(),
                (org.springframework.http.HttpEntity<Object>) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn(new ResponseEntity<>("{\"isValid\":true}", HttpStatus.OK));
		Map<String, Object> actualResponse = integralComponentProcessorService.verifyJWTToken("sampleToken");
		Assertions.assertEquals(true, actualResponse.get("isTokenValid"));
	}
	
	@Test
	public void verifyProcessingDetailsResponseForIntegralComponentService() {
		String responseBody = "{\"processingCost\":500.0,\"packagingAndDeliveryCost\":350.0,\"errors\":false}";
		when(this.restTemplate.exchange((String) any(), (org.springframework.http.HttpMethod) any(),
                (org.springframework.http.HttpEntity<Object>) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn(new ResponseEntity<>(responseBody, HttpStatus.OK));
		Map<String , Object> actualResponse = integralComponentProcessorService.getComponentProcessingDetails("Accessory", 1);
		Map<String, Object> actualTotalCostObtained = (Map<String, Object>) actualResponse.get("cost");
		Assertions.assertEquals(850.00, actualTotalCostObtained.get("totalCost"));
	}
	
	@Test
	public void verifyJWTAuthorisationFailedForIntegralComponentProcessorService() {
		when(this.restTemplate.exchange((String) any(), (org.springframework.http.HttpMethod) any(),
                (org.springframework.http.HttpEntity<Object>) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn(new ResponseEntity<>("{\"isValid\":false}", HttpStatus.OK));
		Map<String, Object> actualResponse = integralComponentProcessorService.verifyJWTToken("sampleToken");
		Assertions.assertEquals(false, actualResponse.get("isTokenValid"));
	}
	
	

}
