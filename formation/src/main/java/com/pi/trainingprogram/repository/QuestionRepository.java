package com.pi.trainingprogram.repository;

import com.pi.trainingprogram.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
