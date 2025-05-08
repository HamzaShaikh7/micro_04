package com.connect.inventory_service.repository;

import com.connect.inventory_service.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Query("SELECT COUNT(i) > 0 FROM Inventory i WHERE i.skuCode = ?1 AND i.quantity >= ?2")
    boolean existsBySkuCodeAndQuantityGreaterThanEqual(String skuCode, int quantity);
}
