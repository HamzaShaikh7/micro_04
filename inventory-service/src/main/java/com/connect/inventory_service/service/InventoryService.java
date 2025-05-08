package com.connect.inventory_service.service;

import com.connect.inventory_service.dto.InventoryRequest;
import com.connect.inventory_service.model.Inventory;
import com.connect.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode, Integer quantity) {
        return inventoryRepository.existsBySkuCodeAndQuantityGreaterThanEqual(skuCode, quantity);
    }


    public void addInStock(InventoryRequest inventoryRequest) {

        Inventory inventory = mapToInventory(inventoryRequest);
        inventoryRepository.save(inventory);
    }

    public Inventory mapToInventory(InventoryRequest inventoryRequest){

        Inventory inventory = new Inventory();
        inventory.setQuantity(inventoryRequest.getQuantity());
        inventory.setSkuCode(inventoryRequest.getSkuCode());
        return inventory;
    }
}
