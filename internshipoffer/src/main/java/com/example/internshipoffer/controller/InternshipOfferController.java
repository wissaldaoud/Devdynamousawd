package com.example.internshipoffer.controller;

import com.example.internshipoffer.entities.InternshipOffer;
import com.example.internshipoffer.service.InternshipOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/internships")
public class InternshipOfferController {

    @Autowired
    private InternshipOfferService internshipOfferService;

    // GET: Retrieve all internship offers
    @GetMapping
    public ResponseEntity<List<InternshipOffer>> getAllOffers() {
        List<InternshipOffer> offers = internshipOfferService.getAllOffers();
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    // GET: Retrieve a specific internship offer by ID
    @GetMapping("/{id}")
    public ResponseEntity<InternshipOffer> getOfferById(@PathVariable Integer id) {
        InternshipOffer offer = internshipOfferService.getOfferById(id);
        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    // POST: Create a new internship offer
    @PostMapping
    public ResponseEntity<InternshipOffer> createOffer(@RequestBody InternshipOffer offer) {
        InternshipOffer createdOffer = internshipOfferService.createOffer(offer);
        return new ResponseEntity<>(createdOffer, HttpStatus.CREATED);
    }

    // PUT: Update an existing internship offer
    @PutMapping("/{id}")
    public ResponseEntity<InternshipOffer> updateOffer(@PathVariable Integer id, @RequestBody InternshipOffer offer) {
        InternshipOffer updatedOffer = internshipOfferService.updateOffer(id, offer);
        return new ResponseEntity<>(updatedOffer, HttpStatus.OK);
    }

    // DELETE: Delete an internship offer by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Integer id) {
        internshipOfferService.deleteOffer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
