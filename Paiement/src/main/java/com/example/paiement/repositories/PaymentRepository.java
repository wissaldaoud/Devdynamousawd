package com.example.paiement.repositories;

import com.example.paiement.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository  extends JpaRepository<Payment, Long> {
    List<Payment> findByCustomerEmail(String customerEmail);
    List<Payment> findByStatus(String status);
    Payment findByReference(String reference);
}
