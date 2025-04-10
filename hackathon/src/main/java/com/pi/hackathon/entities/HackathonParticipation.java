package com.pi.hackathon.entities;


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
public class HackathonParticipation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime joinHackathonTime;
    @ManyToOne
    @JoinColumn(name = "hackathon_id", nullable = false)
    @JsonIgnore
    private Hackathon hackathon;
    private int userId;
}
