package com.saifeddine.user.dto;

import com.saifeddine.user.model.Role;

public class AuthResponse {
    private String token;
    private Long userId;
    private String username;
    private String email;
    private Role role;
    private boolean profileComplete;

    // Add this constructor
    public AuthResponse(String token, Long userId, String username,
                        String email, Role role, boolean profileComplete) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
        this.profileComplete = profileComplete;
    }
    // Getters
    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public boolean isProfileComplete() {
        return profileComplete;
    }

    // Setters
    public void setToken(String token) {
        this.token = token;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setProfileComplete(boolean profileComplete) {
        this.profileComplete = profileComplete;
    }

}