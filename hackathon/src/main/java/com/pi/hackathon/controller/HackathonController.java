package com.pi.hackathon.controller;

import com.pi.hackathon.dtos.Post;
import com.pi.hackathon.entities.Hackathon;
import com.pi.hackathon.entities.HackathonParticipation;
import com.pi.hackathon.service.HackathonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/hackathons")
@RequiredArgsConstructor
@CrossOrigin("*")
public class HackathonController {
    private final HackathonService hackathonService;



    @PostMapping
    public ResponseEntity<Hackathon> createHackathon(@RequestBody Hackathon hackathon) {
        Hackathon createdHackathon = hackathonService.createHackathon(hackathon);
        return new ResponseEntity<>(createdHackathon, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hackathon> getHackathonById(@PathVariable int id) {
        return hackathonService.getHackathonById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<Hackathon>> getAllHackathons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Hackathon> hackathons = hackathonService.getAllHackathons(page, size);
        return ResponseEntity.ok(hackathons);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hackathon> updateHackathon(
            @PathVariable int id,
            @RequestBody Hackathon hackathon) {
        try {
            Hackathon updatedHackathon = hackathonService.updateHackathon(id, hackathon);
            return ResponseEntity.ok(updatedHackathon);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHackathon(@PathVariable int id) {
        try {
            hackathonService.deleteHackathon(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<HackathonParticipation>> getHackathonParticipants(@PathVariable int id) {
        try {
            List<HackathonParticipation> participants = hackathonService.getParticipants(id);
            return ResponseEntity.ok(participants);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getStatistics")
    public ResponseEntity<Map<String, Object>> getStatistics(){
        return ResponseEntity.ok(hackathonService.getStatisticsNiceauDiffuclte());
    }

    @GetMapping("/posts")
    public ResponseEntity<Page<Post>> getAllPosts() {
        Page<Post> hackathons = hackathonService.getAllPosts();
        return ResponseEntity.ok(hackathons);
    }

    @GetMapping("/post/{id}")
    public Post getPostById(@PathVariable int id) {
        Post post = hackathonService.getPostById(id);
        return post;
    }

    @GetMapping("/{id}/best-post")
    public List<Post> getBestPosts(@PathVariable int id) {
        return hackathonService.getBestPosts(id);
    }

    @PostMapping("/{id}/best-post/{postId}")
    public ResponseEntity<String> saveBestPost(@PathVariable int id, @PathVariable
    int postId) {
        Post job = hackathonService.getPostById(postId);
        if (job != null) {
            hackathonService.saveBestPost(postId, id);
            return ResponseEntity.status(HttpStatus.OK).body("Post saved as one of the best  successfully.");
        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Post not found with ID: " + postId);
        }
    }
}
