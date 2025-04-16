package com.pi.hackathon.service;

import com.pi.hackathon.entities.Hackathon;
import com.pi.hackathon.entities.HackathonParticipation;
import com.pi.hackathon.repository.HackathonParticipationRepository;
import com.pi.hackathon.repository.HackathonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class HackathonParticipationService {
    private final HackathonParticipationRepository participationRepository;
    private final HackathonRepository hackathonRepository;

    public HackathonParticipation joinHackathon(int hackathonId, int userId) {
        // Get hackathon and check if it exists
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new EntityNotFoundException("Hackathon not found with id: " + hackathonId));

        // Check if user is already participating
        if (participationRepository.existsByHackathonIdAndUserId(hackathonId, userId)) {
            throw new IllegalStateException("User is already participating in this hackathon");
        }

        // Check if hackathon is full
        int currentParticipants = participationRepository.countByHackathonId(hackathonId);
        if (currentParticipants >= hackathon.getNbrPlaces()) {
            throw new IllegalStateException("Hackathon is already full");
        }

        // Check if hackathon hasn't ended
        if (hackathon.getEndTime().before(new Date())) {
            throw new IllegalStateException("Hackathon has already ended");
        }

        // Create new participation
        HackathonParticipation participation = new HackathonParticipation();
        participation.setHackathon(hackathon);
        participation.setUserId(userId);
        participation.setJoinHackathonTime(LocalDateTime.now());

        return participationRepository.save(participation);
    }

    public void unjoinHackathon(int hackathonId, int userId) {
        HackathonParticipation participation = participationRepository
                .findByHackathonIdAndUserId(hackathonId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Participation not found"));

        // Check if hackathon hasn't started yet
        if (participation.getHackathon().getStartTime().before(new Date())) {
            throw new IllegalStateException("Cannot unjoin after hackathon has started");
        }

        participationRepository.delete(participation);
    }

    public List<HackathonParticipation> getUserParticipations(int userId) {
        return participationRepository.findByUserId(userId);
    }

    public List<HackathonParticipation> getHackathonParticipants(int hackathonId) {
        return participationRepository.findByHackathonId(hackathonId);
    }

    public boolean isUserParticipating(int hackathonId, int userId) {
        return participationRepository.existsByHackathonIdAndUserId(hackathonId, userId);
    }

    public int getParticipantCount(int hackathonId) {
        return participationRepository.countByHackathonId(hackathonId);
    }
}
