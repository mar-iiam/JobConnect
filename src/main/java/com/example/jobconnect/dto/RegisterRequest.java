package com.example.jobconnect.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "JOB_SEEKER|EMPLOYER|ADMIN",
            message = "Role must be JOB_SEEKER, EMPLOYER, or ADMIN")
    private String role;

    // Employer fields
    private String companyName;
    private String companyDescription;

    @Pattern(
            regexp = "^(https?://)?([\\w.-]+)+(:\\d+)?(/.*)?$",
            message = "Invalid website URL"
    )
    private String website;

    // JobSeeker fields
    private String skills;
    private String experienceLevel;
}