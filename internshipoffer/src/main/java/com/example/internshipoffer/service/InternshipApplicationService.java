package com.example.internshipoffer.service;

import com.example.internshipoffer.entities.InternshipApplication;
import com.example.internshipoffer.repository.InternshipApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InternshipApplicationService {

    private final InternshipApplicationRepository repository;

    @Autowired
    private MailService mailService;

    public InternshipApplicationService(InternshipApplicationRepository repository) {
        this.repository = repository;
    }

    public InternshipApplication createApplication(InternshipApplication application) {
        InternshipApplication savedApp = repository.save(application);

        // 📩 Mail simple sans récapitulatif
        String to = savedApp.getApplicantEmail();
        String subject = "Candidature reçue";
        String body = "Bonjour " + savedApp.getApplicantName() + ",\n\n" +
                "Votre candidature a bien été enregistrée.\n" +
                "Nous vous contacterons prochainement.\n\n" +
                "Cordialement,\nL’équipe recrutement.";

        mailService.sendEmail(to, subject, body);

        return savedApp;
    }

    public List<InternshipApplication> getAllApplications() {
        return repository.findAll();
    }

    public Optional<InternshipApplication> getApplicationById(Integer id) {
        return repository.findById(id);
    }

    public Optional<InternshipApplication> updateApplication(Integer id, InternshipApplication updates) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setApplicantName(updates.getApplicantName());
                    existing.setApplicantEmail(updates.getApplicantEmail());
                    existing.setCoverLetter(updates.getCoverLetter());
                    return repository.save(existing);
                });
    }

    public void deleteApplication(Integer id) {
        repository.deleteById(id);
    }
}
