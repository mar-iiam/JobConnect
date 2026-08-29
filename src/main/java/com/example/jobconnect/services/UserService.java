package com.example.jobconnect.services;

import com.example.jobconnect.dto.ApiResponse;
import com.example.jobconnect.dto.UpdateProfileRequest;
import com.example.jobconnect.dto.UserProfileResponse;
import com.example.jobconnect.entity.Role;
import com.example.jobconnect.entity.User;
import com.example.jobconnect.exception.ApiException;
import com.example.jobconnect.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserProfileResponse getCurrentUserProfile(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(404, "User not found", "USER_404"));

        return mapToProfileResponse(user);
    }

    public ApiResponse<Void> updateProfile(String username, UpdateProfileRequest request) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(404, "User not found", "USER_404"));

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        // Job Seeker
        if (user.getJobSeeker() != null) {
            if (request.getSkills() != null) {
                user.getJobSeeker().setSkills(request.getSkills());
            }
            if (request.getExperienceLevel() != null) {
                user.getJobSeeker().setExperienceLevel(request.getExperienceLevel());
            }
        }

        // Employer
        if (user.getEmployer() != null) {
            if (request.getCompanyName() != null) {
                user.getEmployer().setCompanyName(request.getCompanyName());
            }
            if (request.getCompanyDescription() != null) {
                user.getEmployer().setCompanyDescription(request.getCompanyDescription());
            }
            if (request.getWebsite() != null) {
                user.getEmployer().setWebsite(request.getWebsite());
            }
        }

        userRepository.save(user);

        return ApiResponse.<Void>builder()
                .statusCode(200)
                .message("Profile updated successfully")
                .build();
    }

    private UserProfileResponse mapToProfileResponse(User user) {

        return UserProfileResponse.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .username(user.getUsername())
                .roles(
                        user.getRoles().stream()
                                .map(Role::getName)
                                .collect(Collectors.toSet())
                )
                .skills(user.getJobSeeker() != null ? user.getJobSeeker().getSkills() : null)
                .experienceLevel(user.getJobSeeker() != null ? user.getJobSeeker().getExperienceLevel() : null)
                .companyName(user.getEmployer() != null ? user.getEmployer().getCompanyName() : null)
                .companyDescription(user.getEmployer() != null ? user.getEmployer().getCompanyDescription() : null)
                .website(user.getEmployer() != null ? user.getEmployer().getWebsite() : null)
                .build();
    }
}
