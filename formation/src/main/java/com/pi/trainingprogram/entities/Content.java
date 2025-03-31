package com.pi.trainingprogram.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    private String contentUrl;
    private int duration; // Duration in minutes

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;
}
