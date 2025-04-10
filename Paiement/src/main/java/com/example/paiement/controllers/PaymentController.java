package com.example.paiement.controllers;

import com.example.paiement.DTO.PaymentRequest;
import com.example.paiement.entities.Payment;
import com.example.paiement.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody PaymentRequest paymentRequest) {
        Payment payment = paymentService.createPayment(paymentRequest);
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

    @PostMapping("/{reference}/process")
    public ResponseEntity<?> processPayment(@PathVariable String reference) {
        Payment payment = paymentService.processPayment(reference);

        if (payment != null) {
            return ResponseEntity.ok(payment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{reference}/cancel")
    public ResponseEntity<?> cancelPayment(@PathVariable String reference) {
        Payment payment = paymentService.cancelPayment(reference);

        if (payment != null) {
            return ResponseEntity.ok(payment);
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Payment cannot be cancelled or not found"));
        }
    }

    @GetMapping("/{reference}")
    public ResponseEntity<Payment> getPaymentByReference(@PathVariable String reference) {
        Payment payment = paymentService.getPaymentByReference(reference);

        if (payment != null) {
            return ResponseEntity.ok(payment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/customer/{email}")
    public ResponseEntity<List<Payment>> getPaymentsByCustomerEmail(@PathVariable String email) {
        List<Payment> payments = paymentService.getPaymentsByCustomerEmail(email);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable String status) {
        List<Payment> payments = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(payments);
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }
}
