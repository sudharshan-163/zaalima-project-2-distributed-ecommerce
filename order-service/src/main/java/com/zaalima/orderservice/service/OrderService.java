package com.zaalima.orderservice.service;

import com.zaalima.orderservice.avro.OrderCreatedEvent;
import com.zaalima.orderservice.entity.Order;
import com.zaalima.orderservice.repository.OrderRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public OrderService(
            OrderRepository orderRepository,
            KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {

        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order createOrder(Order order) {

        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent event = OrderCreatedEvent.newBuilder()
                .setOrderId(savedOrder.getId())
                .setProductId(savedOrder.getProductId())
                .setQuantity(savedOrder.getQuantity())
                .setStatus(savedOrder.getStatus())
                .build();

        kafkaTemplate.send(
                "order-events",
                String.valueOf(savedOrder.getId()),
                event
        );

        return savedOrder;
    }

    public Optional<Order> updateOrder(Long id, Order updatedOrder) {
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    existingOrder.setStatus(updatedOrder.getStatus());

                    Order savedOrder = orderRepository.save(existingOrder);

                    OrderCreatedEvent event = OrderCreatedEvent.newBuilder()
                            .setOrderId(savedOrder.getId())
                            .setProductId(savedOrder.getProductId())
                            .setQuantity(savedOrder.getQuantity())
                            .setStatus(savedOrder.getStatus())
                            .build();

                    kafkaTemplate.send(
                            "order-events",
                            String.valueOf(savedOrder.getId()),
                            event
                    );

                    return savedOrder;
                });
    }

    public boolean deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }

        return false;
    }
}
