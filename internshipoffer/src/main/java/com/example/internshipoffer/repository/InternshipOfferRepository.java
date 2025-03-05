package com.example.internshipoffer.repository;

import com.example.internshipoffer.entities.InternshipOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternshipOfferRepository extends JpaRepository<InternshipOffer, Integer> {
    // You can add custom query methods here if needed (e.g., filter by location, requiredSkills, etc.)
}