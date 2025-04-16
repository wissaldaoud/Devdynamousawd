package com.pi.hackathon.repository;

import com.pi.hackathon.entities.HackathonParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HackathonParticipationRepository extends JpaRepository<HackathonParticipation, Integer> {
    boolean existsByHackathonIdAndUserId(int hackathonId, int userId);
    Optional<HackathonParticipation> findByHackathonIdAndUserId(int hackathonId, int userId);
    int countByHackathonId(int hackathonId);
    List<HackathonParticipation> findByUserId(int userId);
    List<HackathonParticipation> findByHackathonId(int hackathonId);
}
