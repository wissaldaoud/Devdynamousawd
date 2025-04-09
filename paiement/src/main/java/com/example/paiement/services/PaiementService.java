package com.example.paiement.services;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PaiementService {
    @Autowired
    private EmailService emailService;

    public String createCheckoutSession(int trainingProgramId, String currency, String successUrl, String cancelUrl) throws StripeException {
        Stripe.apiKey = "sk_test_51QwnLFP4qxpDybqGH5vcSe9T4Fy0YYcvsOOt7QfyiFgp8Hv6zsHGuL2IJZcdFGlVaDV3svElcJhMbjHxvtG3VcUn00SRFRdszN"; // à sécuriser plus tard

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(currency)
                                                .setUnitAmount(1000L) // 10.00 €
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Formation")
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        Session session = Session.create(params);
        return session.getUrl();
    }

    public byte[] generateInvoice(int trainingProgramId, String userEmail) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("FACTURE"));
        document.add(new Paragraph("Programme de formation ID: " + trainingProgramId));
        document.add(new Paragraph("Utilisateur: " + userEmail));
        document.add(new Paragraph("Montant total : 10.00 €"));
        document.add(new Paragraph("Merci pour votre paiement."));

        document.close();
        return out.toByteArray();
    }
    public void envoyerFactureParEmail(int trainingProgramId, String userEmail) throws Exception {
        byte[] pdf = generateInvoice(trainingProgramId, userEmail);
        emailService.sendInvoiceEmail(userEmail, pdf);
    }
}
