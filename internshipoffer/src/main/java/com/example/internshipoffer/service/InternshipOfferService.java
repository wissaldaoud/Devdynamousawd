package com.example.internshipoffer.service;

import com.example.internshipoffer.entities.InternshipOffer;
import com.example.internshipoffer.repository.InternshipOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class InternshipOfferService {
    @Autowired
    private InternshipOfferRepository internshipOfferRepository;

    // Retrieve all internship offers
    public List<InternshipOffer> getAllOffers() {
        return internshipOfferRepository.findAll();
    }

    // Retrieve a specific internship offer by its ID
    public InternshipOffer getOfferById(Integer id) {
        return internshipOfferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Internship offer not found with id " + id));
    }

    // Create a new internship offer
    public InternshipOffer createOffer(InternshipOffer offer) {
        return internshipOfferRepository.save(offer);
    }

    // Update an existing internship offer
    public InternshipOffer updateOffer(Integer id, InternshipOffer updatedOffer) {
        InternshipOffer existingOffer = getOfferById(id);
        existingOffer.setTitle(updatedOffer.getTitle());
        existingOffer.setDescription(updatedOffer.getDescription());
        existingOffer.setOfferType(updatedOffer.getOfferType());
        existingOffer.setRequiredSkills(updatedOffer.getRequiredSkills());
        existingOffer.setSupervisorName(updatedOffer.getSupervisorName());
        existingOffer.setMinimumGpa(updatedOffer.getMinimumGpa());
        existingOffer.setStipend(updatedOffer.getStipend());
        existingOffer.setLocation(updatedOffer.getLocation());
        existingOffer.setApplicationDeadline(updatedOffer.getApplicationDeadline());
        existingOffer.setStartDate(updatedOffer.getStartDate());
        existingOffer.setEndDate(updatedOffer.getEndDate());
        existingOffer.setPostDate(updatedOffer.getPostDate());
        existingOffer.setExternalData(updatedOffer.getExternalData());
        existingOffer.setAvailability(updatedOffer.getAvailability());
        return internshipOfferRepository.save(existingOffer);
    }

    // Delete an internship offer by its ID
    public void deleteOffer(Integer id) {
        InternshipOffer offer = getOfferById(id);
        internshipOfferRepository.delete(offer);
    }
}
