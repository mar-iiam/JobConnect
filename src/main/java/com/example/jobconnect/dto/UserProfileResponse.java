package com.example.jobconnect.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder

@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

    private String fullName;
    private String email;
    private String username;
    private Set<String> roles;

    // JobSeeker
    private String skills;
    private String experienceLevel;

    // Employer
    private String companyName;
    private String companyDescription;
    private String website;
}
