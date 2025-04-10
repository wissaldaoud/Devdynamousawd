package com.example.reclamation.services;

import com.example.reclamation.entities.Reponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IReponseService {
    List<Reponse> getAllResponses();

    Optional<Reponse> getResponseById(Integer id);

    List<Reponse> getResponsesByComplaintId(Integer idcomplaint);

    Reponse createResponse(Integer complaintId, Reponse response);

    Reponse updateResponse(Integer id, Reponse responseDetails);

    boolean deleteResponse(Integer id);

    List<Reponse> getResponsesByDateRange(LocalDateTime start, LocalDateTime end);

    List<Reponse> getRecentResponses();

    long countByComplaintts_Idcomplaint(Integer idcomplaint);

    double getAverageResponseTime();

    void statistiquesReponses();
}
