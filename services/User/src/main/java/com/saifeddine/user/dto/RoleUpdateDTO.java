package com.saifeddine.user.dto;

public class RoleUpdateDTO {
    private String newRole; // Should be String to receive role name

    // Getters and Setters
    public String getNewRole() {
        return newRole;
    }

    public void setNewRole(String newRole) {
        this.newRole = newRole;
    }
}