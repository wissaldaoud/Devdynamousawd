package com.example.reclamation.controllers;

import com.example.reclamation.entities.*;
import com.example.reclamation.services.IRecclamationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Reclamation")
public class RecclamationController {
    @Autowired
    private IRecclamationService complaintService;

    @GetMapping
    public ResponseEntity<List<Recclamation>> getAllComplaints() {
        return ResponseEntity.ok(complaintService.getAllComplaints());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Recclamation> getComplaintById(@PathVariable Integer id) {
        Optional<Recclamation> complaint = complaintService.getComplaintById(id);
        return complaint.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/anonymous")
    public ResponseEntity<List<Recclamation>> getAnonymousComplaints() {
        return ResponseEntity.ok(complaintService.getAnonymousComplaints());
    }

    @GetMapping("/non-anonymous")
    public ResponseEntity<List<Recclamation>> getNonAnonymousComplaints() {
        return ResponseEntity.ok(complaintService.getNonAnonymousComplaints());
    }
    @PostMapping
    public ResponseEntity<Recclamation> createComplaint(@RequestBody Recclamation complaint) {
        Recclamation newComplaint = complaintService.createComplaint(complaint);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComplaint);
    }

    @PostMapping("/anonymous")
    public ResponseEntity<Recclamation> createAnonymousComplaint(@RequestBody Recclamation complaint) {
        Recclamation newComplaint = complaintService.createAnonymousComplaint(complaint);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComplaint);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recclamation> updateComplaint(
            @PathVariable Integer id,
            @RequestBody Recclamation complaintDetails) {
        Recclamation updatedComplaint = complaintService.updateComplaint(id, complaintDetails);
        return updatedComplaint != null ?
                ResponseEntity.ok(updatedComplaint) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComplaint(@PathVariable Integer id) {
        return complaintService.deleteComplaint(id) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }
    @PutMapping("/{id}/solve")
    public ResponseEntity<Recclamation> markAsSolved(@PathVariable Integer id) {
        Recclamation solvedComplaint = complaintService.markAsSolved(id);
        return solvedComplaint != null ?
                ResponseEntity.ok(solvedComplaint) :
                ResponseEntity.notFound().build();
    }
    @GetMapping("/date-range")
    public ResponseEntity<List<Recclamation>> getComplaintsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(complaintService.getComplaintsByDateRange(start, end));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Recclamation>> getRecentComplaints() {
        return ResponseEntity.ok(complaintService.getRecentComplaints());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Recclamation>> getComplaintsByType(@PathVariable TypeCom type) {
        return ResponseEntity.ok(complaintService.getComplaintsByType(type));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getComplaintsCount() {
        return ResponseEntity.ok(complaintService.getComplaintsCount());
    }

    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> getComplaintsCountByStatus(@PathVariable Statuscom status) {
        return ResponseEntity.ok(complaintService.getComplaintsCountByStatus(status));
    }

    @PutMapping("/{id}/decline")
    public ResponseEntity<Recclamation> declineComplaint(@PathVariable Integer id) {
        Recclamation declinedComplaint = complaintService.declineComplaint(id);
        return declinedComplaint != null ?
                ResponseEntity.ok(declinedComplaint) :
                ResponseEntity.notFound().build();
    }
    @GetMapping("/statistics")
    public ResponseEntity<StatisticsDTO> getStatistics() {
        StatisticsDTO statistics = complaintService.statistiquesPlaintes();
        return ResponseEntity.ok(statistics);
    }
    @GetMapping("/filter")
    public List<Recclamation> filterComplaintsFromNewToOld(
            @RequestParam("newDate") String newDate,
            @RequestParam("oldDate") String oldDate) {
        LocalDateTime newDateTime = LocalDateTime.parse(newDate);
        LocalDateTime oldDateTime = LocalDateTime.parse(oldDate);
        return complaintService.getComplaintsFromNewToOld(newDateTime, oldDateTime);
    }
    @GetMapping("/search")
    public ResponseEntity<List<Recclamation>> searchComplaints(@RequestParam(required = false) String title) {
        List<Recclamation> results = complaintService.searchComplaintsByTitle(title);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Recclamation>> getComplaintsByPriority(@PathVariable ComplaintPriority priority) {
        return ResponseEntity.ok(complaintService.getComplaintsByPriority(priority));
    }
}
