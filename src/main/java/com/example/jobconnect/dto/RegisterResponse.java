package com.example.jobconnect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Response returned after a registration attempt")
public class RegisterResponse {

    @Schema(description = "HTTP status code of the result", example = "201")
    private Integer statusCode;

    @Schema(description = "Human-readable result message", example = "User registered successfully")
    private String message;

    @Schema(description = "Machine-readable error code, present only when the request failed",
            example = "EMAIL_ALREADY_EXISTS", nullable = true)
    private String errorCode;
}