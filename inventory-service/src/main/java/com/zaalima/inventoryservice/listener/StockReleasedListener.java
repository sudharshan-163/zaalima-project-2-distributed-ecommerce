package com.zaalima.inventoryservice.listener;

import com.zaalima.inventoryservice.event.StockReleasedEvent;
import com.zaalima.inventoryservice.service.InventoryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class StockReleasedListener {

    private final InventoryService inventoryService;

    public StockReleasedListener(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(
            topics = "inventory-events",
            containerFactory = "stockReleasedKafkaListenerContainerFactory",
            groupId = "inventory-release-group"
    )
    public void handleStockReleased(StockReleasedEvent event) {

        if (event.getOrderId() == null
                || event.getProductId() == null
                || event.getQuantity() == null
                || event.getStatus() == null) {
            return;
        }

        if ("RELEASED".equalsIgnoreCase(event.getStatus())) {
            inventoryService.releaseStock(event);
        }
    }
}
