package com.zaalima.inventoryservice.listener;

import com.zaalima.inventoryservice.event.OrderEvent;
import com.zaalima.inventoryservice.service.InventoryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final InventoryService inventoryService;

    public OrderEventListener(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(
            topics = "order-events",
            groupId = "inventory-service"
    )
    public void consumeOrderEvent(OrderEvent orderEvent) {

        System.out.println(
                "Received order event: orderId="
                        + orderEvent.getId()
                        + ", productId="
                        + orderEvent.getProductId()
                        + ", quantity="
                        + orderEvent.getQuantity()
                        + ", status="
                        + orderEvent.getStatus()
        );

        if ("CREATED".equalsIgnoreCase(orderEvent.getStatus())) {
            inventoryService.processOrder(orderEvent);
        }
    }
}