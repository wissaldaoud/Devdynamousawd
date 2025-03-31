package com.pi.trainingprogram.repository;

import com.pi.trainingprogram.entities.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
}
