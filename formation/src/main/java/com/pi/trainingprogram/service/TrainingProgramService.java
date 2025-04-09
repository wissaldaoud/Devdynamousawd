package com.pi.trainingprogram.service;

import com.pi.trainingprogram.entities.TrainingProgram;
import com.pi.trainingprogram.repository.TrainingProgramRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class TrainingProgramService {

    private final TrainingProgramRepository trainingProgramRepository;

    public List<TrainingProgram> getAllTrainingPrograms() {
        return trainingProgramRepository.findAll();
    }

    public TrainingProgram getTrainingProgramById(int id) {
        return trainingProgramRepository.findById(id).orElse(null);
    }

    public TrainingProgram createTrainingProgram(TrainingProgram trainingProgram) {
        return trainingProgramRepository.save(trainingProgram);
    }

    public TrainingProgram updateTrainingProgram(int id, TrainingProgram trainingProgram) {
        if (trainingProgramRepository.existsById(id)) {
            trainingProgram.setId(id);
            return trainingProgramRepository.save(trainingProgram);
        }
        throw new EntityNotFoundException("Training Program not found");
    }

    public void deleteTrainingProgram(int id) {
        trainingProgramRepository.deleteById(id);
    }
    public List<TrainingProgram> searchTrainingPrograms(String keyword) {
        return trainingProgramRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
    }
    public List<TrainingProgram> getSortedPrograms(String sortBy) {
        return switch (sortBy) {
            case "price" -> trainingProgramRepository.findAllByOrderByPriceAsc();
            case "duration" -> trainingProgramRepository.findAllByOrderByDurationAsc();
            default -> trainingProgramRepository.findAll();
        };
    }
    @GetMapping("/export/{id}")
    public ResponseEntity<byte[]> exportTrainingProgramPdf(@PathVariable int id) throws IOException {
        byte[] pdf = trainingProgramService.generatePdfForProgram(id);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=training_" + id + ".pdf")
                .header("Content-Type", "application/pdf")
                .body(pdf);
    }



}
