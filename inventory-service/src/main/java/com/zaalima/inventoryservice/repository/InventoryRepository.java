package com.zaalima.inventoryservice.repository;

import com.zaalima.inventoryservice.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
