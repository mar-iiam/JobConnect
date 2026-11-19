package com.example.jobconnect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Payload for updating the logged-in user's profile. Send only the fields you want to change. Job-seeker and employer fields apply to the matching role.")
public class UpdateProfileRequest {

    @Schema(description = "Full name of the user", example = "Ahmed Hassan",
            minLength = 3, maxLength = 100, nullable = true)
    @Size(min = 3, max = 100)
    private String fullName;

    @Schema(description = "Email address", example = "ahmed@example.com", nullable = true)
    @Email
    private String email;

    // JobSeeker
    @Schema(description = "Comma-separated list of skills (job seekers only)",
            example = "Java, Spring Boot, SQL", nullable = true)
    private String skills;

    @Schema(description = "Experience level (job seekers only)", example = "JUNIOR", nullable = true)
    private String experienceLevel;

    // Employer
    @Schema(description = "Company name (employers only)", example = "Tech Solutions Ltd", nullable = true)
    private String companyName;

    @Schema(description = "Short description of the company (employers only)",
            example = "Software company building HR tools", nullable = true)
    private String companyDescription;

    @Schema(description = "Company website URL (employers only)",
            example = "https://www.techsolutions.com", nullable = true)
    private String website;
}