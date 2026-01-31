package com.example.jobconnect.dto;


import lombok.Data;

@Data
public class RegisterRequest {

    private String fullName;
    private String email;
    private String username;
    private String password;

    // JOB_SEEKER / EMPLOYER / ADMIN
    private String role;

    // Optional fields (based on role)
    private String companyName;        // Employer
    private String companyDescription;
    private String website;

    private String skills;             // JobSeeker
    private String experienceLevel;
}
