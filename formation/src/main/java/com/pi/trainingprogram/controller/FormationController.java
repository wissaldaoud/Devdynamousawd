package com.pi.trainingprogram.controller;



import com.pi.trainingprogram.entities.TrainingProgram;
import com.pi.trainingprogram.service.TrainingProgramService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}

