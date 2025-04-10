package com.example.paiement.services;

import com.example.paiement.DTO.PaymentRequest;
import com.example.paiement.entities.Payment;
import com.example.paiement.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private NotificationService notificationService;

    public Payment createPayment(PaymentRequest paymentRequest) {
        // Génération d'une référence unique
        String reference = "PAY-" + UUID.randomUUID().toString().substring(0, 8);

        Payment payment = new Payment(
                reference,
                paymentRequest.getAmount(),
                paymentRequest.getCurrency(),
                paymentRequest.getCustomerEmail(),
                paymentRequest.getPaymentMethod()
        );

        Payment savedPayment = paymentRepository.save(payment);

        // Envoyer notification pour la création du paiement
        notificationService.sendPaymentNotification(savedPayment, "Paiement créé avec succès");

        return savedPayment;
    }

    public Payment processPayment(String reference) {
        Payment payment = paymentRepository.findByReference(reference);

        if (payment != null) {
            // Simuler le traitement d'un paiement
            payment.setStatus("COMPLETED");
            payment.setUpdatedAt(LocalDateTime.now());
            Payment processedPayment = paymentRepository.save(payment);

            // Envoyer notification pour le traitement du paiement
            notificationService.sendPaymentNotification(processedPayment, "Paiement traité avec succès");

            return processedPayment;
        }

        return null;
    }

    public Payment cancelPayment(String reference) {
        Payment payment = paymentRepository.findByReference(reference);

        if (payment != null && "PENDING".equals(payment.getStatus())) {
            payment.setStatus("CANCELLED");
            payment.setUpdatedAt(LocalDateTime.now());
            Payment cancelledPayment = paymentRepository.save(payment);

            // Envoyer notification pour l'annulation du paiement
            notificationService.sendPaymentNotification(cancelledPayment, "Paiement annulé");

            return cancelledPayment;
        }

        return null;
    }

    public Payment getPaymentByReference(String reference) {
        return paymentRepository.findByReference(reference);
    }

    public List<Payment> getPaymentsByCustomerEmail(String customerEmail) {
        return paymentRepository.findByCustomerEmail(customerEmail);
    }

    public List<Payment> getPaymentsByStatus(String status) {
        return paymentRepository.findByStatus(status);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
