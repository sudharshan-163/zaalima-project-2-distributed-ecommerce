package com.zaalima.paymentservice.event;

import com.zaalima.paymentservice.entity.Payment;
import com.zaalima.paymentservice.repository.PaymentRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderEventListener(
            PaymentRepository paymentRepository,
            KafkaTemplate<String, Object> kafkaTemplate) {
        this.paymentRepository = paymentRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(
            topics = "inventory-events",
            groupId = "payment-service-group"
    )
    public void handleOrderEvent(StockReservedEvent event) {

        System.out.println(
                "Received order event: orderId=" + event.getOrderId()
                        + ", productId=" + event.getProductId()
                        + ", quantity=" + event.getQuantity()
                        + ", status=" + event.getStatus()
        );

        if (event.getOrderId() == null
                || event.getProductId() == null
                || event.getQuantity() == null
                || event.getStatus() == null) {
            return;
        }

        if ("RESERVED".equalsIgnoreCase(event.getStatus())) {

            Payment payment = new Payment(
                    event.getOrderId(),
                    0.0,
                    "SUCCESS"
            );

            paymentRepository.save(payment);

            kafkaTemplate.send(
                    "payment-events",
                    com.zaalima.paymentservice.avro.PaymentSuccessEvent.newBuilder()
                            .setOrderId(event.getOrderId())
                            .setProductId(event.getProductId())
                            .setQuantity(event.getQuantity())
                            .setStatus("SUCCESS")
                            .build()
            );

            System.out.println(
                    "Payment created successfully for orderId="
                            + event.getOrderId()
            );
        }
    }
}

