package com.saifeddine.user.controller;

import com.saifeddine.user.dto.ProfileUpdateDTO;
import com.saifeddine.user.dto.RoleRequestDTO;
import com.saifeddine.user.dto.UserDTO;
import com.saifeddine.user.model.Role;
import com.saifeddine.user.model.User;
import com.saifeddine.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    // Create
    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) {
        User createdUser = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // Read
    @GetMapping("getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/{userId}/request-role")
    public ResponseEntity<User> requestRole(
            @PathVariable Long userId,
            @RequestBody RoleRequestDTO roleRequest
    ) {
        User user = userService.requestRole(userId, roleRequest.getRequestedRole());
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}/profile")
    public ResponseEntity<?> updateProfile(
            @PathVariable Long userId,
            @RequestBody ProfileUpdateDTO profileDTO
    ) {
        try {
            User user = userService.updateProfile(userId, profileDTO);
            return ResponseEntity.ok(user);
        } catch (IllegalStateException e) {
            // This catches the "Complete profile only after role approval" exception
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Your role request must be approved before completing your profile");
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable Role role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDTO userDTO
    ) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


}
