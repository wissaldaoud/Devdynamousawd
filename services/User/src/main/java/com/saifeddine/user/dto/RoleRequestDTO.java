package com.saifeddine.user.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class RoleRequestDTO {
    private Long id;
    private String username;
    private String email;

    @Enumerated(EnumType.STRING)
    private String currentRole;

    @Enumerated(EnumType.STRING)
    private String requestedRole;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCurrentRole() { return currentRole; }
    public void setCurrentRole(String currentRole) { this.currentRole = currentRole; }

    public String getRequestedRole() { return requestedRole; }
    public void setRequestedRole(String requestedRole) { this.requestedRole = requestedRole; }
}
