package com.example.microservice.componentProcessor.Proxy;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import feign.Headers;

@FeignClient(name = "JWTAUTHORIZATION")
@Component
public interface AuthClient {
	//@Headers("Content-Type: application/json")
    @GetMapping("/v1/authorize/user/{tokenId}")
    public String getTokenValidity(@PathVariable("tokenId") String token);
    
    
}

