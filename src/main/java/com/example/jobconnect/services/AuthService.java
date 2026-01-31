package com.example.jobconnect.services;

import com.example.jobconnect.dto.ApiResponse;
import com.example.jobconnect.dto.RegisterRequest;
import com.example.jobconnect.entity.*;
import com.example.jobconnect.exception.ApiException;
import com.example.jobconnect.repository.RoleRepository;
import com.example.jobconnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse<Void> register(RegisterRequest request) {

        // 1️⃣ Check duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiException(400, "Email already exists", "USER_001");
        }

        // 2️⃣ Check duplicate username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ApiException(400, "Username already exists", "USER_002");
        }

        // 3️⃣ Validate role
        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() ->
                        new ApiException(400, "Invalid role provided", "ROLE_001")
                );

        // 4️⃣ Create user
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(new HashSet<>()) // <-- initialize here
                .build();

        user.getRoles().add(role);

        // 5️⃣ Create profile based on role
        if ("JOB_SEEKER".equals(role.getName())) {
            JobSeeker jobSeeker = JobSeeker.builder()
                    .skills(request.getSkills())
                    .experienceLevel(request.getExperienceLevel())
                    .user(user)
                    .build();
        if(request.getExperienceLevel()==null){
            throw new ApiException(400, "Experience Level is required", "Experience_001");
        }
            user.setJobSeeker(jobSeeker);
        }


        if ("EMPLOYER".equals(role.getName())) {
            Employer employer = Employer.builder()
                    .companyName(request.getCompanyName())
                    .companyDescription(request.getCompanyDescription())
                    .website(request.getWebsite())
                    .user(user)
                    .build();

            if(request.getCompanyName()==null){
                throw new ApiException(400, "Company name is required", "C_NAME_001");
            }
            user.setEmployer(employer);
        }

        // 6️⃣ Save user (cascade saves profile)
        userRepository.save(user);

        // 7️⃣ Success response
        return ApiResponse.<Void>builder()
                .statusCode(201)
                .message("User registered successfully")
                .errorCode(null)
                .data(null)
                .build();
    }
}
