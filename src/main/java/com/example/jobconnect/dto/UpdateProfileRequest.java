package com.example.jobconnect.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @Size(min = 3, max = 100)
    private String fullName;

    @Email
    private String email;

    // JobSeeker
    private String skills;
    private String experienceLevel;

    // Employer
    private String companyName;
    private String companyDescription;
    private String website;
}
