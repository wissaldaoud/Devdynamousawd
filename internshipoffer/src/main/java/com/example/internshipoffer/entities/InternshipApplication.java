package com.example.internshipoffer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InternshipApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // The time when the student applied for the internship
    private LocalDateTime applicationTime;

    // Many applications can be linked to one internship offer
    // Application status using the enumeration
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
    @ManyToOne
    @JoinColumn(name = "internship_offer_id", nullable = false)
    @JsonIgnore
    private InternshipOffer internshipOffer;
    // For testing purposes, we use a static user ID (instead of a full User relationship)
    private int userId;
}
