package com.zaalima.paymentservice.service;

import com.zaalima.paymentservice.dto.PaymentFailureRequest;
import com.zaalima.paymentservice.entity.Payment;
import com.zaalima.paymentservice.event.PaymentResultEvent;
import com.zaalima.paymentservice.repository.PaymentRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, PaymentResultEvent> kafkaTemplate;

    public PaymentService(
            PaymentRepository paymentRepository,
            KafkaTemplate<String, PaymentResultEvent> kafkaTemplate) {
        this.paymentRepository = paymentRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Optional<Payment> updatePayment(Long id, Payment updatedPayment) {
        return paymentRepository.findById(id).map(payment -> {
            payment.setOrderId(updatedPayment.getOrderId());
            payment.setAmount(updatedPayment.getAmount());
            payment.setStatus(updatedPayment.getStatus());
            return paymentRepository.save(payment);
        });
    }

    public boolean deletePayment(Long id) {
        if (!paymentRepository.existsById(id)) {
            return false;
        }

        paymentRepository.deleteById(id);
        return true;
    }

    public void publishPaymentFailure(PaymentFailureRequest request) {

        if (request.getOrderId() == null
                || request.getProductId() == null
                || request.getQuantity() == null) {
            return;
        }

        Payment payment =
                new Payment(
                        request.getOrderId(),
                        0.0,
                        "FAILED"
                );

        paymentRepository.save(payment);

        PaymentResultEvent event =
                new PaymentResultEvent(
                        request.getOrderId(),
                        request.getProductId(),
                        request.getQuantity(),
                        "FAILED"
                );

        kafkaTemplate.send(
                "payment-events",
                String.valueOf(request.getOrderId()),
                event
        );
    }
}
