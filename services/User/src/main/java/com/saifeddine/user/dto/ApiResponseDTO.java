package com.saifeddine.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class ApiResponseDTO {
    private boolean success;
    private String message;
    private Object data;

    public ApiResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiResponseDTO(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static ApiResponseDTO success(String message) {
        return new ApiResponseDTO(true, message);
    }

    public static ApiResponseDTO success(String message, Object data) {
        return new ApiResponseDTO(true, message, data);
    }

    public static ApiResponseDTO error(String message) {
        return new ApiResponseDTO(false, message);
    }
}
