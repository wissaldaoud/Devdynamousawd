package com.saifeddine.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Username is required")
    private String userName;

    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.PENDING; // Default role

    @Enumerated(EnumType.STRING)
    private Role requestedRole;

    // Add getters/setters for requestedRole

    public Role getRequestedRole() { return requestedRole; }
    public void setRequestedRole(Role requestedRole) { this.requestedRole = requestedRole; }

    @Column(nullable = false, unique = true)
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean enabled;

    private String phoneNumber;

    // STUDENT-specific attributes
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean paid;

    private String cv;
    private String competence;

    // Common attributes for STUDENT/TEACHER
    private String specialty;

    // ENTERPRISE-specific attributes
    private String address;
    private String sector;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean verified;

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
    public String getPhoneNumber() {return this.phoneNumber;}

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
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}


    @Transient
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Transient
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Transient
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Transient
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Transient
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }
}