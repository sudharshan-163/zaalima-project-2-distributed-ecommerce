package com.zaalima.orderservice.service;

import com.zaalima.orderservice.entity.Order;
import com.zaalima.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> updateOrder(Long id, Order updatedOrder) {
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    existingOrder.setStatus(updatedOrder.getStatus());
                    return orderRepository.save(existingOrder);
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