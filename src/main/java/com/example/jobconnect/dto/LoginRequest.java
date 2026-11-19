package com.example.jobconnect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Login credentials")
public record LoginRequest(

        @Schema(description = "Registered username", example = "ahmed")
        @NotBlank(message = "Username is required")
        String username,

        @Schema(description = "Account password", example = "P@ssw0rd123", format = "password")
        @NotBlank(message = "Password is required")
        String password
) {}