package com.pi.trainingprogram.service;

import com.pi.trainingprogram.entities.Content;
import com.pi.trainingprogram.repository.ContentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;

    public List<Content> getAllContents() {
        return contentRepository.findAll();
    }

    public Content getContentById(int id) {
        return contentRepository.findById(id).orElse(null);
    }

    public Content createContent(Content content) {
        return contentRepository.save(content);
    }

    public Content updateContent(int id, Content content) {
        if (contentRepository.existsById(id)) {
            return contentRepository.save(content);
        }
        throw new EntityNotFoundException("Content not found");
    }

    public void deleteContent(int id) {
        contentRepository.deleteById(id);
    }
}
