package com.example.paiement.controllers;

import com.example.paiement.entities.Paiement;
import com.example.paiement.services.PaiementService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment") // Doit matcher avec ton gateway
@CrossOrigin(origins = "*")
public class PaiementController {

    @Autowired
    private PaiementService paiementService;

    // ✅ Création d'une session Stripe
    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody Map<String, Object> request) throws StripeException {
        int trainingProgramId = (int) request.get("trainingProgramId");
        String currency = (String) request.get("currency");

        String sessionUrl = paiementService.createCheckoutSession(
                trainingProgramId,
                currency,
                "http://localhost:4200/success",
                "http://localhost:4200/cancel"
        );

        return ResponseEntity.ok(Map.of("url", sessionUrl));
    }

    // Optionnel : enregistrer un paiement

}
