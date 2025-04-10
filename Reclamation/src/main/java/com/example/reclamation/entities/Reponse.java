package com.example.reclamation.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Reponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idresponse;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "response_date")
    private LocalDateTime date = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "idcomplaint")
    @JsonIgnore
    private Recclamation complaintts;
    public int getIdresponse() {
        return idresponse;
    }

    public void setIdresponse(int idresponse) {
        this.idresponse = idresponse;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Recclamation getComplaintts() {
        return complaintts;
    }

    public void setComplaintts(Recclamation complaintts) {
        this.complaintts = complaintts;
    }

    // toString method
    @Override
    public String toString() {
        return "Response{" +
                "idresponse=" + idresponse +
                ", message='" + message + '\'' +
                ", date=" + date +
                ", complaintts=" + complaintts +
                '}';
    }
}
