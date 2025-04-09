package com.pi.trainingprogram.controller;

import com.pi.trainingprogram.entities.TrainingProgram;
import com.pi.trainingprogram.service.TrainingProgramService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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

    @GetMapping("/search")
    public List<TrainingProgram> search(@RequestParam String keyword) {
        return trainingProgramService.searchTrainingPrograms(keyword);
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



    @GetMapping("/random")
    public TrainingProgram recommendRandomProgram() {
        return trainingProgramService.getRandomProgram();
    }
}
