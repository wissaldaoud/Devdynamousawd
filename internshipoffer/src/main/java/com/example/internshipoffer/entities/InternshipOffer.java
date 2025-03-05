package com.example.internshipoffer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.internshipoffer.entities.Availability;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//import javax.persistance.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "internship_offer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Title of the internship offer
    private String title;

    // Detailed description of the internship offer
    @Column(length = 2000)
    private String description;

    // Type of internship offer (e.g., "Final Year Project")
    private String offerType;

    // Comma-separated list of required skills
    private String requiredSkills;

    // Name of the supervisor/mentor for the project
    private String supervisorName;

    // Minimum GPA required for eligibility (if applicable)
    private Double minimumGpa;

    // Stipend offered (if any)
    private Double stipend;

    // Location of the internship (e.g., "Remote" or a physical address)
    private String location;

    // Deadline for application submission
    private LocalDate applicationDeadline;

    // Internship start date
    private LocalDate startDate;

    // Internship end date
    private LocalDate endDate;

    // The date when the offer was posted; useful for trend analysis over time
    private LocalDate postDate;

    // Field to store external enrichment data (e.g., salary benchmarks, company ratings) as JSON or text
    @Column(length = 2000)
    private String externalData;

    // Indicates if the offer is currently available
    @Enumerated(EnumType.STRING)
    private Availability availability;
    @OneToMany(mappedBy = "internshipOffer", cascade = CascadeType.ALL)
    @JsonIgnore // Avoid circular references in JSON (optional)
    private List<InternshipApplication> internshipApplications = new ArrayList<>();
}