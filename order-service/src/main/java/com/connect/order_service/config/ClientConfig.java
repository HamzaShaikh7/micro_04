package com.connect.order_service.config;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "inventory-service", url = "http://localhost:8083")
public interface ClientConfig {

    @GetMapping("/api/inventory")
    Boolean checkInventory(@RequestParam("skuCode") String skuCode, @RequestParam("quantity") int quantity);

}
