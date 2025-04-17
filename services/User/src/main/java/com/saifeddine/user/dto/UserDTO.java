package com.saifeddine.user.dto;

import com.saifeddine.user.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserDTO {
    private Long id;

    @NotNull
    @NotBlank(message = "Username is required")
    private String userName;

    @NotNull
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull
    @NotNull(message = "Role is required")
    private Role role;

    // Attributs pour STUDENT
    private Boolean paid;

    //@NotNull(message = "CV is required for STUDENT role", groups = StudentValidationGroup.class)
    private String cv;

    private String competence;

    // Attribut pour STUDENT et TEACHER
    private String specialty;

    // Attributs pour ENTERPRISE
    private String address;

    private String sector;

    private Boolean verified;

    //@NotNull(message = "Business code is required for ENTERPRISE role", groups = EnterpriseValidationGroup.class)
    private String businessCode;

    private String taxId;

    // Manual Getters
    public Long getId() { return id; }
    public String getUserName() { return userName; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
    public Boolean getPaid() { return paid; }
    public String getCv() { return cv; }
    public String getCompetence() { return competence; }
    public String getSpecialty() { return specialty; }
    public String getAddress() { return address; }
    public String getSector() { return sector; }
    public Boolean getVerified() { return verified; }
    public String getBusinessCode() { return businessCode; }
    public String getTaxId() { return taxId; }

    // Manual Setters
    public void setId(Long id) { this.id = id; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(Role role) { this.role = role; }
    public void setPaid(Boolean paid) { this.paid = paid; }
    public void setCv(String cv) { this.cv = cv; }
    public void setCompetence(String competence) { this.competence = competence; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public void setAddress(String address) { this.address = address; }
    public void setSector(String sector) { this.sector = sector; }
    public void setVerified(Boolean verified) { this.verified = verified; }
    public void setBusinessCode(String businessCode) { this.businessCode = businessCode; }
    public void setTaxId(String taxId) { this.taxId = taxId; }

    public UserDTO() {}

    public UserDTO(Long id, String userName, String email, Role role) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.role = role;
    }


}
