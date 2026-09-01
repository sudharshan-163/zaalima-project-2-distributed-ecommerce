package com.zaalima.inventoryservice.service;

import com.zaalima.inventoryservice.entity.Inventory;
import com.zaalima.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public Optional<Inventory> getInventoryById(Long id) {
        return inventoryRepository.findById(id);
    }

    public Inventory createInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public Optional<Inventory> updateInventory(Long id, Inventory updatedInventory) {
        return inventoryRepository.findById(id).map(inventory -> {
            inventory.setProductId(updatedInventory.getProductId());
            inventory.setQuantity(updatedInventory.getQuantity());
            return inventoryRepository.save(inventory);
        });
    }

    public boolean deleteInventory(Long id) {
        if (!inventoryRepository.existsById(id)) {
            return false;
        }

        inventoryRepository.deleteById(id);
        return true;
    }
}
