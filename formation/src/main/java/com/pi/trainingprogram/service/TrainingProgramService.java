package com.pi.trainingprogram.service;

import com.pi.trainingprogram.entities.TrainingProgram;
import com.pi.trainingprogram.repository.TrainingProgramRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
}
