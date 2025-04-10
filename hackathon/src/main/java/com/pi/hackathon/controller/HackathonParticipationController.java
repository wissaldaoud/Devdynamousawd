package com.pi.hackathon.controller;

import com.pi.hackathon.entities.HackathonParticipation;
import com.pi.hackathon.service.HackathonParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hackathons")
@RequiredArgsConstructor
@CrossOrigin("*")
public class HackathonParticipationController {
    private final HackathonParticipationService participationService;

    @PostMapping("/join")
    public ResponseEntity<HackathonParticipation
                > joinHackathon(
            @RequestParam int hackathonId,
            @RequestParam int userId) {
        HackathonParticipation participation = participationService.joinHackathon(hackathonId, userId);
        return ResponseEntity.ok(participation);
    }

    @DeleteMapping("/unjoin")
    public ResponseEntity<Void> unjoinHackathon(
            @RequestParam int hackathonId,
            @RequestParam int userId) {
        participationService.unjoinHackathon(hackathonId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HackathonParticipation>> getUserParticipations(
            @PathVariable int userId) {
        return ResponseEntity.ok(participationService.getUserParticipations(userId));
    }

    @GetMapping("/hackathon/{hackathonId}")
    public ResponseEntity<List<HackathonParticipation>> getHackathonParticipants(
            @PathVariable int hackathonId) {
        return ResponseEntity.ok(participationService.getHackathonParticipants(hackathonId));
    }

    @GetMapping("/count/{hackathonId}")
    public ResponseEntity<Integer> getParticipantCount(
            @PathVariable int hackathonId) {
        return ResponseEntity.ok(participationService.getParticipantCount(hackathonId));
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> checkParticipationStatus(
            @RequestParam int hackathonId,
            @RequestParam int userId) {
        return ResponseEntity.ok(participationService.isUserParticipating(hackathonId, userId));
    }

}
