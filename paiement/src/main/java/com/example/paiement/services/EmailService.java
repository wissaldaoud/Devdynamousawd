package com.example.paiement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendInvoiceEmail(String toEmail, byte[] pdfData) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("Votre facture pour la formation");
        helper.setText("Bonjour,\n\nVeuillez trouver ci-joint votre facture au format PDF.\n\nMerci pour votre paiement.", true);
        helper.addAttachment("facture.pdf", new ByteArrayResource(pdfData));

        mailSender.send(message);
    }
}
