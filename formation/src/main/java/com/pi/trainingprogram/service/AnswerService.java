package com.pi.trainingprogram.service;

import com.pi.trainingprogram.entities.Answer;
import com.pi.trainingprogram.repository.AnswerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    public List<Answer> getAllAnswers() {
        return answerRepository.findAll();
    }

    public Answer getAnswerById(int id) {
        return answerRepository.findById(id).orElse(null);
    }

    public Answer createAnswer(Answer answer) {
        return answerRepository.save(answer);
    }

    public Answer updateAnswer(int id, Answer answer) {
        if (answerRepository.existsById(id)) {
            return answerRepository.save(answer);
        }
        throw new EntityNotFoundException("Answer not found");
    }

    public void deleteAnswer(int id) {
        answerRepository.deleteById(id);
    }
}
