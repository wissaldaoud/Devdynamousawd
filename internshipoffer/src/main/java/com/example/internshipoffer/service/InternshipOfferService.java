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

    @Autowired
    private MailService mailService;

    public List<InternshipOffer> getAllOffers() {
        return internshipOfferRepository.findAll();
    }

    public InternshipOffer getOfferById(Integer id) {
        return internshipOfferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Internship offer not found with id " + id));
    }

    public InternshipOffer createOffer(InternshipOffer offer) {
        InternshipOffer savedOffer = internshipOfferRepository.save(offer);

        // 📨 Envoi de mail après création
        String to = "dorsafriahi6@gmail.com"; // Remplace par l'adresse réelle du RH
        String subject = "Nouvelle Offre de Stage - " + savedOffer.getTitle();
        String text = "Bonjour,\n\nUne nouvelle offre de stage vient d'être créée avec les détails suivants :\n\n" +
                "🔹 Titre : " + savedOffer.getTitle() + "\n" +
                "📄 Description : " + savedOffer.getDescription() + "\n" +
                "📍 Lieu : " + savedOffer.getLocation() + "\n" +
                "🧠 Compétences requises : " + savedOffer.getRequiredSkills() + "\n" +
                "👤 Encadrant : " + savedOffer.getSupervisorName() + "\n" +
                "📅 Date de début : " + savedOffer.getStartDate() + "\n" +
                "📅 Date de fin : " + savedOffer.getEndDate() + "\n" +
                "🕒 Deadline de candidature : " + savedOffer.getApplicationDeadline() + "\n" +
                "💶 Gratification : " + (savedOffer.getStipend() != null ? savedOffer.getStipend() + "€" : "Non précisé") + "\n" +
                "✅ Disponibilité : " + savedOffer.getAvailability() + "\n\n" +
                "Merci de consulter la plateforme pour plus de détails.";

        mailService.sendEmail(to, subject, text);

        return savedOffer;
    }

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

    public void deleteOffer(Integer id) {
        InternshipOffer offer = getOfferById(id);
        internshipOfferRepository.delete(offer);
    }
}
