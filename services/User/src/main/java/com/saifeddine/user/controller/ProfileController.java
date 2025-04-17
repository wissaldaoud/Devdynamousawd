package com.saifeddine.user.controller;

import com.saifeddine.user.dto.ProfileUpdateDTO;
import com.saifeddine.user.model.User;
import com.saifeddine.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    @Autowired  // Explicit constructor injection
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> completeProfile(
            @PathVariable Long userId,
            @RequestBody ProfileUpdateDTO profileDTO
    ) {
        User updatedUser = userService.completeUserProfile(userId, profileDTO);
        return ResponseEntity.ok(updatedUser);
    }
}
