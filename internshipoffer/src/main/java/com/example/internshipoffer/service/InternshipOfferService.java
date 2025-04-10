package com.example.internshipoffer.service;

import com.example.internshipoffer.entities.InternshipOffer;
import com.example.internshipoffer.repository.InternshipOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service gérant la logique métier des offres de stage.
 * Fournit des méthodes pour récupérer, créer, mettre à jour et supprimer des offres de stage.
 */
@Service
public class InternshipOfferService {

    @Autowired
    private InternshipOfferRepository internshipOfferRepository;

    /**
     * Récupère la liste de toutes les offres de stage.
     * @return Une liste d'offres de stage.
     */
    public List<InternshipOffer> getAllOffers() {
        return internshipOfferRepository.findAll();
    }

    /**
     * Récupère une offre de stage par son identifiant.
     * @param id L'ID de l'offre.
     * @return L'offre de stage correspondante ou lance une RuntimeException si l'offre n'est pas trouvée.
     */
    public InternshipOffer getOfferById(Integer id) {
        return internshipOfferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Internship offer not found with id " + id));
    }

    /**
     * Crée une nouvelle offre de stage.
     * @param offer L'offre de stage à créer.
     * @return L'offre de stage créée.
     */
    public InternshipOffer createOffer(InternshipOffer offer) {
        return internshipOfferRepository.save(offer);
    }

    /**
     * Met à jour une offre de stage existante.
     * @param id L'ID de l'offre à mettre à jour.
     * @param updatedOffer L'objet contenant les nouvelles valeurs.
     * @return L'offre de stage mise à jour.
     */
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

    /**
     * Supprime une offre de stage par son ID.
     * @param id L'ID de l'offre à supprimer.
     */
    public void deleteOffer(Integer id) {
        InternshipOffer offer = getOfferById(id);
        internshipOfferRepository.delete(offer);
    }
}
