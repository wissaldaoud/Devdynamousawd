package com.example.reclamation.repositories;

import com.example.reclamation.entities.Reponse;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReponseRepository extends CrudRepository<Reponse, Integer> {
    List<Reponse> findByComplaintts_Idcomplaint(Integer idcomplaint);
    List<Reponse> findByDateBetween(LocalDateTime start, LocalDateTime end);
    List<Reponse> findAllByOrderByDateDesc();
    long countByComplaintts_Idcomplaint(Integer idcomplaint);
}
