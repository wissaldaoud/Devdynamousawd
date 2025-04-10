package com.example.reclamation.services;

import com.example.reclamation.entities.Recclamation;
import com.example.reclamation.entities.Reponse;
import com.example.reclamation.entities.Statuscom;
import com.example.reclamation.repositories.RecclamationRepository;
import com.example.reclamation.repositories.ReponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReponseService  implements IReponseService{


    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ReponseService.class);
    @Autowired
    private ReponseRepository responseRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private RecclamationRepository complaintRepository;
    @Override
    public List<Reponse> getAllResponses() {
        List<Reponse> responses = new ArrayList<>();
        responseRepository.findAll().forEach(responses::add);
        return  responses;
    }

    @Override
    public Optional<Reponse> getResponseById(Integer id) {
        return responseRepository.findById(id);
    }

    @Override
    public List<Reponse> getResponsesByComplaintId(Integer idcomplaint) {
        return   responseRepository.findByComplaintts_Idcomplaint(idcomplaint);
    }

    @Override
    public Reponse createResponse(Integer idcomplaint, Reponse response) {
        return complaintRepository.findById(idcomplaint).map(complaint -> {
            response.setComplaintts(complaint);
            // S'assurer que la date est définie
            if (response.getDate() == null) {
                response.setDate(LocalDateTime.now());
            }
            // Marquer la plainte comme résolue après réponse
            complaint.setStatus(Statuscom.SOLVED);
            complaintRepository.save(complaint);

            // Save the response
            Reponse savedResponse = responseRepository.save(response);

            // Send the email notification
            sendResponseEmail(complaint, savedResponse);

            return savedResponse;
        }).orElse(null);
    }

    private void sendResponseEmail(Recclamation complaint, Reponse response) {
        try {
            String userEmail = complaint.getMail(); // Get email from Complaint

            if (userEmail == null || userEmail.isEmpty()) {
                log.warn("No email found for complaint ID: {}", complaint.getIdcomplaint());
                return;
            }

            String subject = "Response to Your Complaint: " + complaint.getTitle();
            String body = String.format(
                    "Dear User,\n\n" +
                            "We have responded to your complaint:\n\n" +
                            "**Complaint Title**: %s\n" +
                            "**Your Message**: %s\n\n" +
                            "**Our Response**: %s\n\n" +
                            "Thank you for your feedback.\n\n" +
                            "Best regards,\nThe Support Team",
                    complaint.getTitle(),
                    complaint.getDescription(),
                    response.getMessage()
            );

            emailService.sendEmail(userEmail, subject, body);
            log.info("Notification sent to: {}", userEmail);

        } catch (Exception e) {
            log.error("Failed to send email for complaint ID {}: {}",
                    complaint.getIdcomplaint(), e.getMessage());
        }
    }

    @Override
    public Reponse updateResponse(Integer id, Reponse responseDetails) {
        Optional<Reponse> responseOpt = responseRepository.findById(id);
        if (responseOpt.isPresent()) {
            Reponse response = responseOpt.get();
            response.setMessage(responseDetails.getMessage());
            // Ne pas modifier la date de création lors de la mise à jour
            return responseRepository.save(response);
        }
        return null;
    }

    @Override
    public boolean deleteResponse(Integer id) {
        if (responseRepository.existsById(id)) {
            responseRepository.deleteById(id);
            return true;
        }
        return false;
    }
    @Override
    public List<Reponse> getResponsesByDateRange(LocalDateTime start, LocalDateTime end) {
        return  responseRepository.findByDateBetween(start, end);
    }

    @Override
    public List<Reponse> getRecentResponses() {
        return responseRepository.findAllByOrderByDateDesc();
    }

    @Override
    public long countByComplaintts_Idcomplaint(Integer idcomplaint) {
        return  responseRepository.countByComplaintts_Idcomplaint(idcomplaint);
    }

    @Override
    public double getAverageResponseTime() {
        List<Recclamation> solvedComplaints = complaintRepository.findByStatus(Statuscom.SOLVED);
        if (solvedComplaints.isEmpty()) {
            return 0;
        }
        double totalHours = 0;
        int count = 0;

        for (Recclamation complaint : solvedComplaints) {
            List<Reponse> responses = responseRepository.findByComplaintts_Idcomplaint(complaint.getIdcomplaint());
            if (!responses.isEmpty()) {
                // Prendre la première réponse pour calculer le temps de réponse
                Reponse firstResponse = responses.get(0);
                Duration duration = Duration.between(complaint.getDate(), firstResponse.getDate());
                totalHours += duration.toHours();
                count++;
            }
        }

        return count > 0 ? totalHours / count : 0;
    }

    @Override
    public void statistiquesReponses() {
        System.out.println("=== STATISTIQUES DES RÉPONSES ===");

        // Nombre total de réponses
        long totalResponses = 0;
        Iterable<Reponse> allResponses = responseRepository.findAll();
        for (Reponse response : allResponses) {
            totalResponses++;
        }
        System.out.println("Nombre total de réponses: " + totalResponses);

        // Temps moyen de réponse
        double avgResponseTime = getAverageResponseTime();
        System.out.println("Temps moyen de réponse: " + String.format("%.2f", avgResponseTime) + " heures");

        // Nombre de plaintes sans réponse
        long pendingComplaints = complaintRepository.countByStatus(Statuscom.PENDING);
        System.out.println("Plaintes en attente de réponse: " + pendingComplaints);

        // Taux de réponse
        long totalComplaints = complaintRepository.count();
        double responseRate = totalComplaints > 0 ?
                (double) (totalComplaints - pendingComplaints) / totalComplaints * 100 : 0;
        System.out.println("Taux de réponse: " + String.format("%.2f", responseRate) + "%");

    }
}
