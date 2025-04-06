package com.example.paiement.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Service;

@Service
public class PaiementService {

    public String createCheckoutSession(int trainingProgramId, String currency, String successUrl, String cancelUrl) throws StripeException {
        Stripe.apiKey = "sk_test_51QwnLFP4qxpDybqGH5vcSe9T4Fy0YYcvsOOt7QfyiFgp8Hv6zsHGuL2IJZcdFGlVaDV3svElcJhMbjHxvtG3VcUn00SRFRdszN"; // Remplace par ta clé secrète Stripe

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl) // URL après succès
                .setCancelUrl(cancelUrl)   // URL si annulé
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(currency)
                                .setUnitAmount(1000L) // Prix en centimes (ex: 10.00€)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Formation")
                                        .build())
                                .build())
                        .build())
                .build();

        Session session = Session.create(params);
        return session.getUrl();
    }
}
