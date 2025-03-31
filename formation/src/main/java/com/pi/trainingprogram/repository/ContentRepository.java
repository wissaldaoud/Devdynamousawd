package com.pi.trainingprogram.repository;

import com.pi.trainingprogram.entities.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Integer> {
}
