package com.example.reclamation.services;

import com.example.reclamation.entities.*;
import com.example.reclamation.repositories.RecclamationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.*;

@Service
public class RecclamationService implements IRecclamationService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RecclamationService.class);

    @Autowired
    private RecclamationRepository complaintRepository;
    private final Set<String> highPriorityKeywords = new HashSet<>(Arrays.asList(
            "urgent", "critique", "immédiat", "panne", "bloquant",
            "sécurité", "risque", "légal", "financier",
            "production", "client important", "danger", "catastrophe",
            "error in course","bug in the site","bad words in forum",
            "the payment dosn't work","the payment"
    ));

    private final Set<String> mediumPriorityKeywords = new HashSet<>(Arrays.asList(
            "problème", "dysfonctionnement", "amélioration",
            "demande", "incompréhension", "confusion",
            "délai", "retard", "impact", "significatif"
    ));

    private final Set<String> lowPriorityKeywords = new HashSet<>(Arrays.asList(
            "information", "suggestion", "conseil", "question",
            "curiosité", "non urgent", "faible impact", "secondaire",
            "optionnel", "exploratoire", "minimal", "trivial","advice","be instructor"
    ));
    private ComplaintPriority classifyComplaintPriority(Recclamation complaint) {
        // Convertir le titre et la description en minuscules pour la comparaison
        String fullText = (complaint.getTitle() + " " + complaint.getDescription()).toLowerCase();

        // Compteurs pour la classification
        int highPriorityScore = 0;
        int mediumPriorityScore = 0;
        int lowPriorityScore = 0;

        // Vérifier les mots-clés de haute priorité
        for (String keyword : highPriorityKeywords) {
            if (fullText.contains(keyword)) {
                highPriorityScore += 2;
            }
        }

        // Vérifier les mots-clés de moyenne priorité
        for (String keyword : mediumPriorityKeywords) {
            if (fullText.contains(keyword)) {
                mediumPriorityScore += 1;
            }
        }
        for (String keyword : lowPriorityKeywords) {
            if (fullText.contains(keyword)) {
                lowPriorityScore += 1;
            }
        }

        // Déterminer la priorité en fonction des scores
        if (highPriorityScore >= 2) {
            return ComplaintPriority.HIGH;
        } else if (mediumPriorityScore >= 2 || highPriorityScore == 1) {
            return ComplaintPriority.MEDIUM;
        } else if (lowPriorityScore >= 1) {
            return ComplaintPriority.LOW;
        } else {
            return ComplaintPriority.MEDIUM; // Défaut à MEDIUM si aucun mot-clé ne correspond
        }
    }


    @Override
    public List<Recclamation> getAllComplaints() {
        List<Recclamation> complaints = new ArrayList<>();
        complaintRepository.findAll().forEach(complaints::add);
        return complaints;
    }

    @Override
    public Recclamation declineComplaint(Integer id) {
        Optional<Recclamation> complaintOpt = complaintRepository.findById(id);
        if (complaintOpt.isPresent()) {
            Recclamation complaint = complaintOpt.get();
            complaint.setStatus(Statuscom.DECLINED); // Set the status to DECLINED
            return complaintRepository.save(complaint); // Save the updated complaint
        }
        return null; // Or throw an exception if preferred
    }

    @Override
    public Optional<Recclamation> getComplaintById(Integer id) {
        return complaintRepository.findById(id);
    }

    @Override
    public List<Recclamation> getComplaintsByStatus(Statuscom status) {
        return complaintRepository.findByStatus(status);
    }

    @Override
    public List<Recclamation> getComplaintsByMail(String mail) {
        return complaintRepository.findByMail(mail);
    }

    @Override
    public Recclamation createComplaint(Recclamation complaint) {
        // Définir la priorité automatiquement
        complaint.setPriority(classifyComplaintPriority(complaint));
        // Définir le statut par défaut comme PENDING
        complaint.setStatus(Statuscom.PENDING);
        // S'assurer que la date est définie
        if (complaint.getDate() == null) {
            complaint.setDate(LocalDateTime.now());
        }
        if (complaint.getIsAnonymous() == null) {
            complaint.setIsAnonymous(false);
        }
        return complaintRepository.save(complaint);
    }

    @Override
    public Recclamation createAnonymousComplaint(Recclamation complaint) {
        // Anonymiser la plainte
        anonymizeComplaint(complaint);
        // Définir la priorité automatiquement
        complaint.setPriority(classifyComplaintPriority(complaint));

        // Définir le statut par défaut comme PENDING
        complaint.setStatus(Statuscom.PENDING);

        // S'assurer que la date est définie
        if (complaint.getDate() == null) {
            complaint.setDate(LocalDateTime.now());
        }

        // Marquer explicitement comme anonyme
        complaint.setIsAnonymous(true);

        log.info("Création d'une plainte anonyme");
        return complaintRepository.save(complaint);
    }

    private void anonymizeComplaint(Recclamation complaint) {
        // Remplacer les informations personnelles par des valeurs anonymes
        complaint.setMail("anonyme@systeme.interne");

        // Générer un identifiant unique pour cette plainte anonyme si nécessaire
        complaint.setAnonymousId(UUID.randomUUID().toString());

        // Ne pas modifier le titre et la description pour préserver les détails de la plainte
        log.debug("Plainte anonymisée avec succès");
    }

    @Override
    public Recclamation updateComplaint(Integer id, Recclamation complaintDetails) {
        Optional<Recclamation> complaintOpt = complaintRepository.findById(id);
        if (complaintOpt.isPresent()) {
            Recclamation complaint = complaintOpt.get();
            complaint.setDescription(complaintDetails.getDescription());
            complaint.setMail(complaintDetails.getMail());
            complaint.setType(complaintDetails.getType());
            complaint.setTitle(complaintDetails.getTitle());
            // Ne pas modifier la date de création lors de la mise à jour
            return complaintRepository.save(complaint);
        }
        return null;
    }

    @Override
    public boolean deleteComplaint(Integer id) {
        if (complaintRepository.existsById(id)) {
            complaintRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Recclamation markAsSolved(Integer id) {
        Optional<Recclamation> complaintOpt = complaintRepository.findById(id);
        if (complaintOpt.isPresent()) {
            Recclamation complaint = complaintOpt.get();
            complaint.setStatus(Statuscom.SOLVED);
            return complaintRepository.save(complaint);
        }
        return null;
    }

    @Override
    public List<Recclamation> getComplaintsByPriority(ComplaintPriority priority) {
        return complaintRepository.findByPriority(priority);
    }

    @Override
    public List<Recclamation> getComplaintsByDateRange(LocalDateTime start, LocalDateTime end) {
        return complaintRepository.findByDateBetween(start, end);
    }

    @Override
    public List<Recclamation> getRecentComplaints() {
        return complaintRepository.findAllByOrderByDateDesc();
    }

    @Override
    public List<Recclamation> getComplaintsByType(TypeCom type) {
        return complaintRepository.findByType(type);
    }

    @Override
    public long getComplaintsCount() {
        return complaintRepository.count();
    }

    @Override
    public List<Recclamation> getAnonymousComplaints() {
        return complaintRepository.findByIsAnonymousTrue();
    }

    @Override
    public List<Recclamation> getNonAnonymousComplaints() {
        return complaintRepository.findByIsAnonymousFalse();
    }

    @Override
    public long getComplaintsCountByStatus(Statuscom status) {
        return complaintRepository.countByStatus(status);
    }

    @Override
    public StatisticsDTO statistiquesPlaintes() {
        StatisticsDTO stats = new StatisticsDTO();
        stats.setTotalComplaints(getComplaintsCount());
        stats.setPendingCount(getComplaintsCountByStatus(Statuscom.PENDING));
        stats.setSolvedCount(getComplaintsCountByStatus(Statuscom.SOLVED));
        stats.setDeclinedCount(getComplaintsCountByStatus(Statuscom.DECLINED));

        // Calculate resolution rate
        double resolutionRate = (double) stats.getSolvedCount() / stats.getTotalComplaints() * 100;
        stats.setResolutionRate(Double.isNaN(resolutionRate) ? 0 : resolutionRate); // Handle division by zero

        return stats;
    }

    public List<Recclamation> getComplaintsFromNewToOld(LocalDateTime newDate, LocalDateTime oldDate) {
        return complaintRepository.findByDateBetweenOrderByDateDesc(newDate, oldDate);
    }

    public List<Recclamation> searchComplaintsByTitle(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            // If search term is empty, return all complaints sorted by date
            return complaintRepository.findAllByOrderByDateDesc();
        }
        return complaintRepository.findByTitleContainingIgnoreCase(searchTerm);
    }
}