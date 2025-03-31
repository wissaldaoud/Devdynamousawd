package com.pi.trainingprogram.repository;

import com.pi.trainingprogram.entities.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
}
