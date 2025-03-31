package com.pi.trainingprogram.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private int duration;
    private String prerequisites;
    private String objectives;
    private double price;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private int trainerId;

    @OneToMany(mappedBy = "trainingProgram", cascade = CascadeType.ALL)
    private List<Module> modules;

}

