package com.saifeddine.user.service;
import com.saifeddine.user.dto.ForgotPasswordRequestDTO;
import com.saifeddine.user.dto.ResetPasswordRequestDTO;
import com.saifeddine.user.model.PasswordResetToken;
import com.saifeddine.user.model.User;
import com.saifeddine.user.repository.PasswordResetTokenRepository;
import com.saifeddine.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class PasswordResetService {
    private static final Logger log = LoggerFactory.getLogger(PasswordResetService.class);

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    private static final int TOKEN_EXPIRY_HOURS = 24;
    private static final SecureRandom secureRandom = new SecureRandom();

    // Constructor injection
    public PasswordResetService(
            UserRepository userRepository,
            PasswordResetTokenRepository tokenRepository,
            EmailService emailService,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }
    /**
     * Initiates password reset process by generating token and sending email
     */
    public void initiateForgotPassword(ForgotPasswordRequestDTO request) {
        String email = request.getEmail().toLowerCase().trim();

        // Check if user exists
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            // For security reasons, don't reveal if email exists or not
            log.warn("Password reset requested for non-existent email: {}", email);
            return; // Still return success to prevent email enumeration
        }

        User user = userOpt.get();

        // Invalidate any existing tokens for this email
        tokenRepository.markAllTokensAsUsedByEmail(email);

        // Generate new token
        String token = generateSecureToken();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(TOKEN_EXPIRY_HOURS);

        // Save token to database
        PasswordResetToken resetToken = new PasswordResetToken(token, email, expiryDate);
        tokenRepository.save(resetToken);

        // Send email
        try {
            emailService.sendPasswordResetEmail(email, token, user.getUserName());
            log.info("Password reset email sent for user: {}", email);
        } catch (Exception e) {
            log.error("Failed to send password reset email for user: {}", email, e);
            // Delete the token if email failed to send
            tokenRepository.delete(resetToken);
            throw new RuntimeException("Failed to send password reset email");
        }
    }

    /**
     * Validates reset token
     */
    public boolean validateResetToken(String token) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);

        if (tokenOpt.isEmpty()) {
            return false;
        }

        PasswordResetToken resetToken = tokenOpt.get();
        return !resetToken.isUsed() && !resetToken.isExpired();
    }

    /**
     * Resets user password using valid token
     */
    public void resetPassword(ResetPasswordRequestDTO request) {
        // Validate passwords match
        if (!request.isPasswordMatching()) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Find and validate token
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(request.getToken());
        if (tokenOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid or expired reset token");
        }

        PasswordResetToken resetToken = tokenOpt.get();

        // Check if token is used or expired
        if (resetToken.isUsed()) {
            throw new IllegalArgumentException("Reset token has already been used");
        }

        if (resetToken.isExpired()) {
            throw new IllegalArgumentException("Reset token has expired");
        }

        // Find user by email
        User user = userRepository.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Mark token as used
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        // Invalidate all other tokens for this email
        tokenRepository.markAllTokensAsUsedByEmail(resetToken.getEmail());

        log.info("Password successfully reset for user: {}", user.getEmail());
    }

    /**
     * Generates a secure random token
     */
    private String generateSecureToken() {
        byte[] tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    /**
     * Scheduled task to clean up expired tokens (runs daily at 2 AM)
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupExpiredTokens() {
        try {
            tokenRepository.deleteExpiredTokens(LocalDateTime.now());
            log.info("Expired password reset tokens cleaned up");
        } catch (Exception e) {
            log.error("Error during token cleanup", e);
        }
    }

    /**
     * Get token info for validation (useful for frontend)
     */
    public Optional<PasswordResetToken> getTokenInfo(String token) {
        return tokenRepository.findByToken(token);
    }
}