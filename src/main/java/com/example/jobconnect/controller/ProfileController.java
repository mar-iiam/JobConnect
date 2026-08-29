package com.example.jobconnect.controller;

import com.example.jobconnect.dto.*;
import com.example.jobconnect.services.AuthService;
import com.example.jobconnect.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping("/me")
    public UserProfileResponse getProfile(Authentication authentication) {
        return userService.getCurrentUserProfile(authentication.getName());
    }

    @PutMapping
    public ApiResponse<Void> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request) {

        return userService.updateProfile(authentication.getName(), request);
    }


}
