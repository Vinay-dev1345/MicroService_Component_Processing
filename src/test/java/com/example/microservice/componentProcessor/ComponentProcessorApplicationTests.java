package com.example.microservice.componentProcessor;


import static org.mockito.Mockito.*;

import java.io.StringReader;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
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

import com.example.microservice.componentProcessor.Proxy.AuthClient;
import com.example.microservice.componentProcessor.Proxy.CostComputeClient;
import com.example.microservice.componentProcessor.controller.ComponentProcessorController;
import com.example.microservice.componentProcessor.entity.ComponentOrderProcessorEntity;
import com.example.microservice.componentProcessor.repository.ComponentProcessorRepository;
import com.example.microservice.componentProcessor.service.impl.AccessoryComponentProcessorService;
import com.example.microservice.componentProcessor.service.impl.ComponentOrderProcessorService;
import com.example.microservice.componentProcessor.service.impl.IntegralComponentProcessorService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ComponentProcessorApplicationTests {

	@Mock
	ComponentProcessorRepository componentProcessorRepository;
	
	@Mock
	AuthClient authClient;
	
	@Mock
	CostComputeClient costComputeClient;
	
	@Mock
	JsonObject jso;
	
	@Mock
	ComponentOrderProcessorEntity order;
	
	@InjectMocks
	AccessoryComponentProcessorService accessoryComponentProcessorService;
	
	@InjectMocks
	IntegralComponentProcessorService integralComponentProcessorService;
	
	@InjectMocks
	ComponentOrderProcessorService componentOrderProcessorService;
	
	@InjectMocks
	ComponentProcessorController componentProcessorController;
	
	@Test
	public void verifyJWTAuthorisationForAccessoryComponentProcessorService() {
		when(this.authClient.getTokenValidity((String) any())).thenReturn("{\"isValid\":true}");
		Map<String, Object> actualResponse = accessoryComponentProcessorService.verifyJWTToken("sampleToken");
		Assertions.assertEquals(true, actualResponse.get("isTokenValid"));
	}
	
	@Test
	public void verifyProcessingDetailsResponseForAccessoryComponentService() {
		String responseBody = "{\"processingCost\":300.0,\"packagingAndDeliveryCost\":200.0,\"errors\":false}";
		when(this.costComputeClient.getComponentCost((String) any(), (String) any())).thenReturn(responseBody);
		Map<String , Object> actualResponse = accessoryComponentProcessorService.getComponentProcessingDetails("Accessory", 1);
		Map<String, Object> actualTotalCostObtained = (Map<String, Object>) actualResponse.get("cost");
		Assertions.assertEquals(500.00, actualTotalCostObtained.get("totalCost"));
	}
	
	@Test
	public void verifyJWTAuthorisationFailedForAccessoryComponentProcessorService() {
		when(this.authClient.getTokenValidity((String) any())).thenReturn("{\"isValid\":false}");
		Map<String, Object> actualResponse = accessoryComponentProcessorService.verifyJWTToken("sampleToken");
		Assertions.assertEquals(false, actualResponse.get("isTokenValid"));
	}
	
	@Test
	public void verifyJWTAuthorisationForIntegralComponentProcessorService() {
		when(this.authClient.getTokenValidity((String) any())).thenReturn("{\"isValid\":true}");
		Map<String, Object> actualResponse = integralComponentProcessorService.verifyJWTToken("sampleToken");
		Assertions.assertEquals(true, actualResponse.get("isTokenValid"));
	}
	
	@Test
	public void verifyProcessingDetailsResponseForIntegralComponentService() {
		String responseBody = "{\"processingCost\":500.0,\"packagingAndDeliveryCost\":350.0,\"errors\":false}";
		when(this.costComputeClient.getComponentCost((String) any(), (String) any())).thenReturn(responseBody);
		Map<String , Object> actualResponse = integralComponentProcessorService.getComponentProcessingDetails("Accessory", 1);
		Map<String, Object> actualTotalCostObtained = (Map<String, Object>) actualResponse.get("cost");
		Assertions.assertEquals(850.00, actualTotalCostObtained.get("totalCost"));
	}
	
	@Test
	public void verifyJWTAuthorisationFailedForIntegralComponentProcessorService() {
		when(this.authClient.getTokenValidity((String) any())).thenReturn("{\"isValid\":false}");
		Map<String, Object> actualResponse = integralComponentProcessorService.verifyJWTToken("sampleToken");
		Assertions.assertEquals(false, actualResponse.get("isTokenValid"));
	}
	
	@Test
	public void verifyJWTAuthorisationForComponentOrderProcessorService() {
		when(this.authClient.getTokenValidity((String) any())).thenReturn("{\"isValid\":true}");
		Map<String, Object> actualResponse = componentOrderProcessorService.verifyJWTToken("sampleToken");
		Assertions.assertEquals(true, actualResponse.get("isTokenValid"));
	}
	
	@Test
	public void verifyJWTFailedAuthorisationForComponentOrderProcessorService() {
		when(this.authClient.getTokenValidity((String) any())).thenReturn("{\"isValid\":false}");
		Map<String, Object> actualResponse = componentOrderProcessorService.verifyJWTToken("sampleToken");
		Assertions.assertEquals(false, actualResponse.get("isTokenValid"));
	}
	
	@Test
	public void verifyWhetherTheOrderIsSavedOrNot() {
		String sampleOrderBody = "{\"isConversionToOrder\":true,\"orderId\":\"61966234\",\"customerDetails\":{\"customerName\":\"jane\",\"contactNumber\":\"+910876544211\"},\"componentDetails\":{\"componentType\":\"accessory\",\"componentName\":\"vacfridge1\",\"extraDetails\":\"fanproblem\",\"qty\":1},\"costingDetails\":{\"packagingAndDeliveryCost\":350.00,\"processingCost\":500.00},\"dateOfDelivery\":\"MonAug0800:51:34IST2022\"}";
		JsonReader jsr = Json.createReader(new StringReader(sampleOrderBody));
		JsonObject jso = jsr.readObject();
		jsr.close();
		
		Map<String, Object> actualResponse = componentOrderProcessorService.createReturnOrder(jso);
		Assertions.assertEquals(true, actualResponse.get("isSavedSuccessfully"));
		Assertions.assertEquals(850.00, actualResponse.get("totalCost"));
		Assertions.assertEquals(false, actualResponse.get("errors"));	
	}
	
	@Test
	public void verifyGetProcessingDetailsWhenInValidComponent() {
		ResponseEntity<?> actualResponse = componentProcessorController.getProcessingDetails("undefined", 1 , "token");
		Map<String , Object> response = (Map<String, Object>) actualResponse.getBody();
		
		Assertions.assertEquals(true , response.get("errors"));
	}
	
	@Test
	public void verifyGetProcessingDetailsWhenOrderCreationIsFailed() {
		String body = "{\"isConversionToOrder\":false,\"orderId\":\"61966234\",\"customerDetails\":{\"customerName\":\"jane\",\"contactNumber\":\"+910876544211\"},\"componentDetails\":{\"componentType\":\"accessory\",\"componentName\":\"vacfridge1\",\"extraDetails\":\"fanproblem\",\"qty\":1},\"costingDetails\":{\"packagingAndDeliveryCost\":350.00,\"processingCost\":500.00},\"dateOfDelivery\":\"MonAug0800:51:34IST2022\"}";
		ResponseEntity<?> actualResponse = componentProcessorController.initiateReturnOrder(body , "token");
		Map<String , Object> response = (Map<String, Object>) actualResponse.getBody();
		
		Assertions.assertEquals(true , response.get("errors"));
	}
	
	@Test
	public void verifyGetProcessingDetailsWhenOrderCreationIsFailedAtTheCreationProcess() {
		String body = "{\"isConversionToOrder\":true,\"orderId\":\"\",\"customerDetails\":{\"customerName\":\"THOR\",\"contactNumber\":\"+910876544211\"},\"componentDetails\":{\"componentType\":\"accessory\",\"componentName\":\"vacfridge1\",\"extraDetails\":\"fanproblem\",\"qty\":1},\"costingDetails\":{\"packagingAndDeliveryCost\":350.00,\"processingCost\":500.00},\"dateOfDelivery\":\"MonAug0800:51:34IST2022\"}";
		ResponseEntity<?> actualResponse = componentProcessorController.initiateReturnOrder(body , "token");
		Map<String , Object> response = (Map<String, Object>) actualResponse.getBody();
		
		Assertions.assertEquals(true , response.get("errors"));
	}
	
	
	

}
