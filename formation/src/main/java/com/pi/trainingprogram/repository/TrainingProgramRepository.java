package com.pi.trainingprogram.repository;

import com.pi.trainingprogram.entities.TrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, Integer> {
}
