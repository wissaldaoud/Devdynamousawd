package com.example.internshipoffer.repository;

import com.example.internshipoffer.entities.InternshipApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour l'entité InternshipApplication.
 * Fournit des méthodes CRUD de base.
 */
@Repository
public interface InternshipApplicationRepository extends JpaRepository<InternshipApplication, Integer> {
    // Vous pouvez ajouter ici des méthodes de recherche personnalisées si nécessaire.
}
