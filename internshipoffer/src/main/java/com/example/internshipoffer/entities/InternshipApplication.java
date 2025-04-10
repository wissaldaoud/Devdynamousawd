package com.example.internshipoffer.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Date et heure de la candidature
    private LocalDateTime applicationTime;

    // Statut de la candidature (PENDING, ACCEPTED, REJECTED, CANCELED)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    // Relation ManyToOne : L'offre de stage associée à cette candidature
    @ManyToOne
    @JoinColumn(name = "internship_offer_id", nullable = false)
    private InternshipOffer internshipOffer;

    // Identifiant de l'utilisateur postulant (peut être remplacé par une relation User)
    private int userId;

    // Nom du candidat
    private String applicantName;

    // Email du candidat
    private String applicantEmail;

    // Lettre de motivation (peut contenir jusqu'à 2000 caractères)
    @Column(length = 2000)
    private String coverLetter;

    // Chemin vers le fichier CV stocké localement (ex: "uploads/1618321234567_cv.pdf")
    private String cvPath;
}
