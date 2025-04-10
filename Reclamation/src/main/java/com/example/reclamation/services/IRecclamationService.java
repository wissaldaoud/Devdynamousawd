package com.example.reclamation.services;

import com.example.reclamation.entities.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IRecclamationService {
    List<Recclamation> getAllComplaints();

    Optional<Recclamation> getComplaintById(Integer id);

    List<Recclamation> getComplaintsByStatus(Statuscom status);

    List<Recclamation> getComplaintsByMail(String mail);

    Recclamation createComplaint(Recclamation complaint);
    Recclamation createAnonymousComplaint(Recclamation complaint);

    Recclamation updateComplaint(Integer id, Recclamation complaintDetails);

    Recclamation declineComplaint(Integer id);

    boolean deleteComplaint(Integer id);

    Recclamation markAsSolved(Integer id);

    List<Recclamation> getComplaintsByDateRange(LocalDateTime start, LocalDateTime end);
    List<Recclamation> getComplaintsFromNewToOld(LocalDateTime newDate, LocalDateTime oldDate);

    List<Recclamation> getRecentComplaints();

    List<Recclamation> searchComplaintsByTitle(String searchTerm);

    List<Recclamation> getComplaintsByType(TypeCom type);
    List<Recclamation> getAnonymousComplaints();

    List<Recclamation> getNonAnonymousComplaints();

    long getComplaintsCount();

    long getComplaintsCountByStatus(Statuscom status);

    List<Recclamation> getComplaintsByPriority(ComplaintPriority priority);

    StatisticsDTO statistiquesPlaintes();
}
