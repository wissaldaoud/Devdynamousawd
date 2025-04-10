package com.example.reclamation.controllers;

import com.example.reclamation.entities.Reponse;
import com.example.reclamation.services.IReponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reponses")
public class ReponseController {
    @Autowired
    IReponseService responseService;
    @GetMapping
    public ResponseEntity<List<Reponse>> getAllResponses() {
        return ResponseEntity.ok(responseService.getAllResponses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reponse> getResponseById(@PathVariable Integer id) {
        Optional<Reponse> response = responseService.getResponseById(id);
        return response.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/Reclamation/{idcomplaint}")
    public ResponseEntity<List<Reponse>> getResponsesByComplaintId(@PathVariable Integer idcomplaint) {
        return ResponseEntity.ok(responseService.getResponsesByComplaintId(idcomplaint));
    }
    @PostMapping("/Reclamation/{idcomplaint}")
    public ResponseEntity<Reponse> createResponse(
            @PathVariable Integer idcomplaint,
            @RequestBody Reponse response) {
        Reponse newResponse = responseService.createResponse(idcomplaint, response);
        return newResponse != null ?
                ResponseEntity.status(HttpStatus.CREATED).body(newResponse) :
                ResponseEntity.notFound().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Reponse> updateResponse(
            @PathVariable Integer id,
            @RequestBody Reponse responseDetails) {
        Reponse updatedResponse = responseService.updateResponse(id, responseDetails);
        return updatedResponse != null ?
                ResponseEntity.ok(updatedResponse) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResponse(@PathVariable Integer id) {
        return responseService.deleteResponse(id) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }
    @GetMapping("/date-range")
    public ResponseEntity<List<Reponse>> getResponsesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(responseService.getResponsesByDateRange(start, end));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Reponse>> getRecentResponses() {
        return ResponseEntity.ok(responseService.getRecentResponses());
    }

    @GetMapping("/count/complaint/{idcomplaint}")
    public ResponseEntity<Long> countResponsesForComplaint(@PathVariable Integer idcomplaint) {
        return ResponseEntity.ok(responseService.countByComplaintts_Idcomplaint(idcomplaint));
    }

    @GetMapping("/average-time")
    public ResponseEntity<Double> getAverageResponseTime() {
        return ResponseEntity.ok(responseService.getAverageResponseTime());
    }

    @GetMapping("/statistics")
    public ResponseEntity<Void> getStatistics() {
        responseService.statistiquesReponses();
        return ResponseEntity.ok().build();
    }
}
