package com.pi.hackathon.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Hackathon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private String location;
    private Date startTime;
    private Date endTime;
    private int nbrPlaces;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime created_at;
    @Enumerated(EnumType.STRING)
    private NiveauDifficulte niveauDifficulte;
    @Enumerated(EnumType.STRING)
    private NiveauImportance niveauImportance;
    @OneToMany(mappedBy = "hackathon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HackathonParticipation> participations;
    @ElementCollection
    private Set<Integer> bestPosts = new HashSet<>();
}
