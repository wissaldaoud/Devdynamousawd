package com.pi.hackathon.repository;

import com.pi.hackathon.entities.Hackathon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HackathonRepository extends JpaRepository<Hackathon, Integer> {
}
