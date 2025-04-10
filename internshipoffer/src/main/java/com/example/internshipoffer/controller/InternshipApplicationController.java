package com.example.internshipoffer.controller;

import com.example.internshipoffer.entities.ApplicationStatus;
import com.example.internshipoffer.entities.InternshipApplication;
import com.example.internshipoffer.entities.InternshipOffer;
import com.example.internshipoffer.service.InternshipApplicationService;
import com.example.internshipoffer.service.InternshipOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/internship-applications")
public class InternshipApplicationController {

    private final InternshipApplicationService applicationService;
    private final InternshipOfferService offerService;

    @Autowired
    public InternshipApplicationController(
            InternshipApplicationService applicationService,
            InternshipOfferService offerService
    ) {
        this.applicationService = applicationService;
        this.offerService = offerService;
    }

    @PostMapping
    public ResponseEntity<?> createApplication(
            @RequestBody InternshipApplication application,
            @RequestParam("internshipOfferId") Integer offerId
    ) {
        try {
            // 1. Valider l'existence de l'offre
            InternshipOffer offer = offerService.getOfferById(offerId);

            // 2. Définir les valeurs par défaut
            application.setApplicationTime(LocalDateTime.now());
            application.setStatus(ApplicationStatus.PENDING);

            // 3. Associer l'offre à la candidature
            application.setInternshipOffer(offer);

            // 4. Sauvegarder
            InternshipApplication savedApplication = applicationService.createApplication(application);

            return new ResponseEntity<>(savedApplication, HttpStatus.CREATED);

        } catch (RuntimeException e) {
            // Gestion d'erreur améliorée
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<InternshipApplication>> getAllApplications() {
        List<InternshipApplication> applications = applicationService.getAllApplications();
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InternshipApplication> getApplicationById(@PathVariable Integer id) {
        return applicationService.getApplicationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<InternshipApplication> updateApplication(
            @PathVariable Integer id,
            @RequestBody InternshipApplication application
    ) {
        return applicationService.updateApplication(id, application)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Integer id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
}