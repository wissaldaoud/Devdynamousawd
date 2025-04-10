package com.example.paiement.services;

import com.example.paiement.DTO.PaymentNotification;
import com.example.paiement.entities.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendPaymentNotification(Payment payment, String message) {
        PaymentNotification notification = new PaymentNotification(
                payment.getReference(),
                payment.getStatus(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getCustomerEmail(),
                message
        );

        // Envoyer la notification à tous les abonnés au canal de paiement général
        messagingTemplate.convertAndSend("/topic/payments", notification);

        // Envoyer la notification au canal spécifique du client
        messagingTemplate.convertAndSend("/topic/payments/" + payment.getCustomerEmail(), notification);
    }
}
