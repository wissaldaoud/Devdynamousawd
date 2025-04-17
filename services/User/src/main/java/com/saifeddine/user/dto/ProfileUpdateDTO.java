package com.saifeddine.user.dto;

public class ProfileUpdateDTO {
    //FOR THE STUDENT
    private Boolean paid;
    //FOR THE STUDENT AND FREELANCER
    private String cv;
    private String competence;
    //FOR THE STUDENT AND TEACHER
    private String specialty;
    //FOR THE ENTREPRISE
    private String businessCode;
    private String taxId;
    private String sector;
    //FOR ALL ROLES
    private String address;
    private String phoneNumber;
    // Getters
    public Boolean getPaid() { return paid; }
    public String getCv() { return cv; }
    public String getCompetence() { return competence; }
    public String getSpecialty() { return specialty; }
    public String getBusinessCode() { return businessCode; }
    public String getTaxId() { return taxId; }
    public String getAddress() { return address; }
    public String getSector() { return sector; }
    public String getPhoneNumber() { return phoneNumber; }

    // Setters
    public void setPaid(Boolean paid) { this.paid = paid; }
    public void setCv(String cv) { this.cv = cv; }
    public void setCompetence(String competence) { this.competence = competence; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public void setBusinessCode(String businessCode) { this.businessCode = businessCode; }
    public void setTaxId(String taxId) { this.taxId = taxId; }
    public void setAddress(String address) { this.address = address; }
    public void setSector(String sector) { this.sector = sector; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
