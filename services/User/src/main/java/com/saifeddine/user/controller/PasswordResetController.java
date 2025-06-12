package com.saifeddine.user.controller;

import com.saifeddine.user.dto.ForgotPasswordRequestDTO;
import com.saifeddine.user.dto.ResetPasswordRequestDTO;
import com.saifeddine.user.dto.ApiResponseDTO;
import com.saifeddine.user.model.PasswordResetToken;
import com.saifeddine.user.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/password")
@CrossOrigin(origins = "*")
public class PasswordResetController {

    // Manual logger creation
    private static final Logger log = LoggerFactory.getLogger(PasswordResetController.class);

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponseDTO> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO request) {
        try {
            passwordResetService.initiateForgotPassword(request);
            return ResponseEntity.ok(
                    ApiResponseDTO.success("If the email exists, a password reset link has been sent")
            );
        } catch (Exception e) {
            log.error("Error processing forgot password request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Failed to process password reset request"));
        }
    }

    /**
     * Endpoint to validate reset token
     */
    @GetMapping("/validate-reset-token")
    public ResponseEntity<ApiResponseDTO> validateResetToken(@RequestParam String token) {
        try {
            boolean isValid = passwordResetService.validateResetToken(token);

            if (isValid) {
                return ResponseEntity.ok(
                        ApiResponseDTO.success("Token is valid")
                );
            } else {
                return ResponseEntity.badRequest()
                        .body(ApiResponseDTO.error("Invalid or expired token"));
            }
        } catch (Exception e) {
            log.error("Error validating reset token", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Failed to validate token"));
        }
    }

    /**
     * Endpoint to reset password
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponseDTO> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO request) {
        try {
            passwordResetService.resetPassword(request);
            return ResponseEntity.ok(
                    ApiResponseDTO.success("Password has been reset successfully")
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error resetting password", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Failed to reset password"));
        }
    }

    /**
     * Endpoint to get token information (useful for frontend validation)
     */
    @GetMapping("/reset-token-info")
    public ResponseEntity<ApiResponseDTO> getTokenInfo(@RequestParam String token) {
        try {
            Optional<PasswordResetToken> tokenInfo = passwordResetService.getTokenInfo(token);

            if (tokenInfo.isPresent()) {
                PasswordResetToken resetToken = tokenInfo.get();

                // Create response data with limited information
                var tokenData = new Object() {
                    public final String email = maskEmail(resetToken.getEmail());
                    public final boolean expired = resetToken.isExpired();
                    public final boolean used = resetToken.isUsed();
                    public final boolean valid = !resetToken.isExpired() && !resetToken.isUsed();
                };

                return ResponseEntity.ok(
                        ApiResponseDTO.success("Token information retrieved", tokenData)
                );
            } else {
                return ResponseEntity.badRequest()
                        .body(ApiResponseDTO.error("Token not found"));
            }
        } catch (Exception e) {
            log.error("Error retrieving token info", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Failed to retrieve token information"));
        }
    }

    /**
     * Masks email for security (shows only first 2 characters and domain)
     */
    private String maskEmail(String email) {
        if (email == null || email.length() < 5) {
            return "***@***.***";
        }

        String[] parts = email.split("@");
        if (parts.length != 2) {
            return "***@***.***";
        }

        String localPart = parts[0];
        String domain = parts[1];

        String maskedLocal = localPart.length() > 2
                ? localPart.substring(0, 2) + "***"
                : "***";

        return maskedLocal + "@" + domain;
    }
}
