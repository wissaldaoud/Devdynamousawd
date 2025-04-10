package com.example.reclamation.repositories;

import com.example.reclamation.entities.ComplaintPriority;
import com.example.reclamation.entities.Recclamation;
import com.example.reclamation.entities.Statuscom;
import com.example.reclamation.entities.TypeCom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RecclamationRepository  extends CrudRepository<Recclamation,Integer > {
    List<Recclamation> findByStatus(Statuscom status);
    List<Recclamation> findByMail(String mail);
    List<Recclamation> findAllByOrderByDateDesc();
    List<Recclamation> findByType(TypeCom type);
    List<Recclamation> findByIsAnonymousTrue();
    List<Recclamation> findByIsAnonymousFalse();

    List<Recclamation> findByPriority(ComplaintPriority priority);
    @Query("SELECT COUNT(c) FROM Recclamation c WHERE c.status = :status")
    long countByStatus(@Param("status") Statuscom status);
    List<Recclamation> findByDateBetween(LocalDateTime start, LocalDateTime end);
    List<Recclamation> findByDateBetweenOrderByDateDesc(LocalDateTime newDate, LocalDateTime oldDate);

    // Search by title containing the search term (case-insensitive)
    // In your repository interface
    @Query("SELECT c FROM Recclamation c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY c.date DESC")
    List<Recclamation> findByTitleContainingIgnoreCase(@Param("searchTerm") String searchTerm);

}
