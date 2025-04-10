package com.example.reclamation.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Recclamation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idcomplaint;
    @Column(columnDefinition = "TEXT")
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(name = "is_anonymous")
    private Boolean isAnonymous = false;

    @Column(name = "anonymous_id")
    private String anonymousId;

    private String mail;

    @Enumerated(EnumType.STRING)
    private TypeCom type;

    @Enumerated(EnumType.STRING)
    private Statuscom status = Statuscom.PENDING; // Par défaut

    @Enumerated(EnumType.STRING)
    private ComplaintPriority priority = ComplaintPriority.MEDIUM;

    @Column(name = "created_date")
    private LocalDateTime date = LocalDateTime.now(); // Date de création, par défaut à maintenant

    @OneToMany(mappedBy = "complaintts", cascade = CascadeType.ALL)
    private List<Reponse> responses;
    public int getIdcomplaint() {
        return idcomplaint;
    }

    public void setIdcomplaint(int idcomplaint) {
        this.idcomplaint = idcomplaint;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public TypeCom getType() {
        return type;
    }

    public void setType(TypeCom type) {
        this.type = type;
    }

    public Statuscom getStatus() {
        return status;
    }

    public void setStatus(Statuscom status) {
        this.status = status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public Boolean getIsAnonymous() { return isAnonymous; }

    public String getAnonymousId() {
        return anonymousId;
    }

    public void setAnonymousId(String anonymousId) {
        this.anonymousId = anonymousId;
    }
    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }
    public ComplaintPriority getPriority() {
        return priority;
    }

    public void setPriority(ComplaintPriority priority) {
        this.priority = priority;
    }

    public List<Reponse> getResponses() {
        return responses;
    }

    public void setResponses(List<Reponse> responses) {
        this.responses = responses;
    }

    // toString method
    @Override
    public String toString() {
        return "Complaintts{" +
                "idcomplaint=" + idcomplaint +
                ", description='" + description + '\'' +
                ", mail='" + mail + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", date=" + date +
                '}';
    }


}
