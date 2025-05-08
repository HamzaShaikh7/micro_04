package com.connect.inventory_service.controller;

import com.connect.inventory_service.dto.InventoryRequest;
import com.connect.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity) {
        return inventoryService.isInStock(skuCode, quantity);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String addInStock(@RequestBody InventoryRequest inventoryRequest) {
        inventoryService.addInStock(inventoryRequest);
        return "Entry updated.";
    }
}
