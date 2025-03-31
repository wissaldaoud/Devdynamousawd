package com.pi.trainingprogram.repository;

import com.pi.trainingprogram.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingModuleRepository extends JpaRepository<Module, Integer> {
}
