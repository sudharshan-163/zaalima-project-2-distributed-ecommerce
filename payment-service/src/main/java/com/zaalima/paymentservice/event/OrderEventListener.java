package com.zaalima.paymentservice.event;

import com.zaalima.paymentservice.entity.Payment;
import com.zaalima.paymentservice.repository.PaymentRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, PaymentResultEvent> kafkaTemplate;

    public OrderEventListener(
            PaymentRepository paymentRepository,
            KafkaTemplate<String, PaymentResultEvent> kafkaTemplate) {
        this.paymentRepository = paymentRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(
            topics = "order-events",
            groupId = "payment-service-group"
    )
    public void handleOrderEvent(OrderEvent event) {

        System.out.println(
                "Received order event: orderId=" + event.getId()
                        + ", productId=" + event.getProductId()
                        + ", quantity=" + event.getQuantity()
                        + ", status=" + event.getStatus()
        );

        if (event.getId() == null || event.getStatus() == null) {
            return;
        }

        if ("CREATED".equals(event.getStatus())) {

            Payment payment = new Payment(
                    event.getId(),
                    0.0,
                    "SUCCESS"
            );

            paymentRepository.save(payment);

            kafkaTemplate.send(
                    "payment-events",
                    new PaymentResultEvent(event.getId(), "SUCCESS")
            );

            System.out.println(
                    "Payment created successfully for orderId="
                            + event.getId()
            );
        }
    }
}
