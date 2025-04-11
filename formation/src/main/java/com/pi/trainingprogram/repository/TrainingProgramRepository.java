package com.pi.trainingprogram.repository;

import com.pi.trainingprogram.entities.TrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, Integer> {
    List<TrainingProgram> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
    List<TrainingProgram> findAllByOrderByPriceAsc();
    List<TrainingProgram> findAllByOrderByDurationAsc();
    List<TrainingProgram> findByCategoryIgnoreCase(String category);


}
