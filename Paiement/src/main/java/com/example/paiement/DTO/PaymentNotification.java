package com.example.paiement.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentNotification {
    private String reference;
    private String status;
    private BigDecimal amount;
    private String currency;
    private String customerEmail;
    private LocalDateTime timestamp;
    private String message;

    public PaymentNotification() {
        this.timestamp = LocalDateTime.now();
    }

    public PaymentNotification(String reference, String status, BigDecimal amount, String currency, String customerEmail, String message) {
        this();
        this.reference = reference;
        this.status = status;
        this.amount = amount;
        this.currency = currency;
        this.customerEmail = customerEmail;
        this.message = message;
    }

    // Getters et setters
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
