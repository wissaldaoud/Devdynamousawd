package com.pi.trainingprogram.controller;

import com.pi.trainingprogram.entities.TrainingProgram;
import com.pi.trainingprogram.service.TrainingProgramService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/formation")
@AllArgsConstructor
public class FormationController {

    private final TrainingProgramService trainingProgramService;

    @GetMapping
    public List<TrainingProgram> getAllTrainingPrograms() {
        return trainingProgramService.getAllTrainingPrograms();
    }

    @GetMapping("/{id}")
    public TrainingProgram getTrainingProgramById(@PathVariable int id) {
        return trainingProgramService.getTrainingProgramById(id);
    }

    @PostMapping
    public TrainingProgram createTrainingProgram(@RequestBody TrainingProgram trainingProgram) {
        return trainingProgramService.createTrainingProgram(trainingProgram);
    }

    @PutMapping("/{id}")
    public TrainingProgram updateTrainingProgram(@PathVariable int id, @RequestBody TrainingProgram trainingProgram) {
        return trainingProgramService.updateTrainingProgram(id, trainingProgram);
    }

    @DeleteMapping("/{id}")
    public void deleteTrainingProgram(@PathVariable int id) {
        trainingProgramService.deleteTrainingProgram(id);
    }



    @GetMapping("/sort")
    public List<TrainingProgram> sortBy(@RequestParam String sortBy) {
        return trainingProgramService.getSortedPrograms(sortBy);
    }


    @GetMapping("/export/{id}")
    public ResponseEntity<byte[]> exportTrainingProgramPdf(@PathVariable int id) throws IOException {
        byte[] pdf = trainingProgramService.generatePdfForProgram(id);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=training_" + id + ".pdf")
                .header("Content-Type", "application/pdf")
                .body(pdf);
    }





    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        return ResponseEntity.ok(trainingProgramService.getStatistics());
    }

    @GetMapping("/count-by-duration")
    public ResponseEntity<?> getCountByDuration() {
        return ResponseEntity.ok(trainingProgramService.getCountByDurationRanges());
    }
    @GetMapping("/filter")
    public ResponseEntity<List<TrainingProgram>> filterPrograms(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minDuration,
            @RequestParam(required = false) Integer maxDuration
    ) {
        return ResponseEntity.ok(trainingProgramService.filterPrograms(minPrice, maxPrice, minDuration, maxDuration));
    }





    @GetMapping("/chatbot")
    public ResponseEntity<String> chatbot(@RequestParam(name = "question") String question) {
        String response = trainingProgramService.faqBot(question);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}/generate-certificate")
    public ResponseEntity<byte[]> generateCertificate(
            @PathVariable int id,
            @RequestParam String name
    ) throws IOException {
        byte[] pdf = trainingProgramService.generateCertificateForUser(id, name);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=certificat_" + name + ".pdf")
                .header("Content-Type", "application/pdf")
                .body(pdf);
    }

    // Liste des formations par catégorie
    @GetMapping("/category")
    public ResponseEntity<List<TrainingProgram>> getByCategory(@RequestParam String name) {
        return ResponseEntity.ok(trainingProgramService.getProgramsByCategory(name));
    }

    // Statistiques par catégorie
    @GetMapping("/category-stats")
    public ResponseEntity<Map<String, Long>> getCategoryStats() {
        return ResponseEntity.ok(trainingProgramService.countByCategory());
    }









    @GetMapping("/chart/pie-category")
    public ResponseEntity<byte[]> getPieChartByCategory() throws IOException {
        byte[] image = trainingProgramService.generatePieChartByCategory();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=chart.png")
                .header("Content-Type", "image/png")
                .body(image);
    }
    @GetMapping("/chart/pdf-category")
    public ResponseEntity<byte[]> getPieChartPdfByCategory() throws IOException {
        byte[] pdf = trainingProgramService.generatePieChartPdfByCategory();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=chart_category.pdf")
                .header("Content-Type", "application/pdf")
                .body(pdf);
    }
    @GetMapping("/search-title")
    public ResponseEntity<List<TrainingProgram>> searchByTitle(@RequestParam String keyword) {
        return ResponseEntity.ok(trainingProgramService.searchByTitle(keyword));
    }












}
