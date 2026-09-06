package com.zaalima.paymentservice.event;

import com.zaalima.paymentservice.entity.Payment;
import com.zaalima.paymentservice.repository.PaymentRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final PaymentRepository paymentRepository;

    public OrderEventListener(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
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

            System.out.println(
                    "Payment created successfully for orderId="
                            + event.getId()
            );
        }
    }
}
