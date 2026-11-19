package com.example.jobconnect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Profile of the logged-in user. Job-seeker or employer fields are filled depending on the user's role.")
public class UserProfileResponse {

    @Schema(description = "Full name of the user", example = "Ahmed Hassan")
    private String fullName;

    @Schema(description = "Email address", example = "ahmed@example.com")
    private String email;

    @Schema(description = "Username used to log in", example = "ahmed")
    private String username;

    @Schema(description = "Roles assigned to the user", example = "[\"JOB_SEEKER\"]")
    private Set<String> roles;

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