package com.example.jobconnect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Payload for registering a new account. Employer-only and job-seeker-only fields are optional and depend on the chosen role.")
public class RegisterRequest {

    @Schema(description = "Full name of the user", example = "Ahmed Hassan", minLength = 3, maxLength = 100)
    @NotBlank(message = "Full name is required")
    @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters")
    private String fullName;

    @Schema(description = "Email address, must be unique", example = "ahmed@example.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "Unique username used to log in", example = "ahmed", minLength = 3, maxLength = 50)
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @Schema(description = "Account password (at least 6 characters)", example = "P@ssw0rd123",
            minLength = 6, maxLength = 100, format = "password")
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
    private String password;

    @Schema(description = "Account role", example = "JOB_SEEKER",
            allowableValues = {"JOB_SEEKER", "EMPLOYER", "ADMIN"})
    @NotBlank(message = "Role is required")
    @Pattern(regexp = "JOB_SEEKER|EMPLOYER|ADMIN",
            message = "Role must be JOB_SEEKER, EMPLOYER, or ADMIN")
    private String role;

    // Employer fields
    @Schema(description = "Company name (employers only)", example = "Tech Solutions Ltd", nullable = true)
    private String companyName;

    @Schema(description = "Short description of the company (employers only)",
            example = "Software company building HR tools", nullable = true)
    private String companyDescription;

    @Schema(description = "Company website URL (employers only)", example = "https://www.techsolutions.com", nullable = true)
    @Pattern(
            regexp = "^(https?://)?([\\w.-]+)+(:\\d+)?(/.*)?$",
            message = "Invalid website URL"
    )
    private String website;

    // JobSeeker fields
    @Schema(description = "Comma-separated list of skills (job seekers only)", example = "Java, Spring Boot, SQL", nullable = true)
    private String skills;

    @Schema(description = "Experience level (job seekers only)", example = "JUNIOR", nullable = true)
    private String experienceLevel;
}