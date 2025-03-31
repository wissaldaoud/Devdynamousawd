package com.pi.trainingprogram.service;

import com.pi.trainingprogram.entities.Question;
import com.pi.trainingprogram.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question getQuestionById(int id) {
        return questionRepository.findById(id).orElse(null);
    }

    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    public Question updateQuestion(int id, Question question) {
        if (questionRepository.existsById(id)) {
            return questionRepository.save(question);
        }
        throw new EntityNotFoundException("Question not found");
    }

    public void deleteQuestion(int id) {
        questionRepository.deleteById(id);
    }
}
