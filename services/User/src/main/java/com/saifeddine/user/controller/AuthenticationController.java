package com.saifeddine.user.controller;

import com.saifeddine.user.dto.AuthResponse;
import com.saifeddine.user.dto.LoginRequest;
import com.saifeddine.user.dto.RegisterRequest;
import com.saifeddine.user.dto.UserDTO;
import com.saifeddine.user.model.User;
import com.saifeddine.user.service.UserService;
import com.saifeddine.user.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    // Manual constructor injection
    @Autowired
    public AuthenticationController(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            UserService userService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    // Login (Sign In)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Extract UserDetails from the authentication object
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Generate JWT token using UserDetails (not the User entity)
            String jwt = jwtUtil.generateToken(userDetails);

            // Fetch additional user details from the database (if needed)
            User user = userService.getUserByEmail(request.getEmail());

            // Build the response
            return ResponseEntity.ok(new AuthResponse(
                    jwt,
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole(),
                    isProfileComplete(user)
            ));

        } catch (BadCredentialsException e) {
            // Return 401 for invalid credentials instead of 500
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username/password");
        }
    }

    // Sign Up
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(request.getUsername());
        userDTO.setPassword(request.getPassword());
        userDTO.setEmail(request.getEmail());
        // Don't set role here - it will be set to PENDING by default in the service

        User newUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    private boolean isProfileComplete(User user) {
        return switch (user.getRole()) {
            case STUDENT -> user.getPaid() != null &&
                    user.getCv() != null &&
                    user.getCompetence() != null &&
                    user.getSpecialty() != null;
            case TEACHER, FREELANCER -> user.getSpecialty() != null;
            case ENTERPRISE -> user.getBusinessCode() != null &&
                    user.getTaxId() != null &&
                    user.getAddress() != null;
            case ADMIN, PENDING -> true; // Admins/Pending users bypass checks
        };
    }
}
