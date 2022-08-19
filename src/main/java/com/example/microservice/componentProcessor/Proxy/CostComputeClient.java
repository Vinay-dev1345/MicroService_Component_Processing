package com.example.microservice.componentProcessor.Proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "PACKAGINGANDDELIVERY")
public interface CostComputeClient {
    @GetMapping("v1/packagingAndDelivery/getcost")
    public String getComponentCost(@RequestParam("type") String productType , @RequestParam("count") String qty);
}
