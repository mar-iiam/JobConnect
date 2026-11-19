package com.example.jobconnect.controller;

import com.example.jobconnect.dto.*;
import com.example.jobconnect.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Profile", description = "View and update the profile of the logged-in user")
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @Operation(
            summary = "Get my profile",
            description = "Returns the profile of the user identified by the JWT."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "Profile retrieved",
                    content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "Missing or invalid token", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/me")
    public UserProfileResponse getProfile(Authentication authentication) {
        return userService.getCurrentUserProfile(authentication.getName());
    }

    @Operation(
            summary = "Update my profile",
            description = "Updates the profile of the authenticated user."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "Profile updated",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "Validation error", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "Missing or invalid token", content = @Content)
    })
    @PutMapping
    public ApiResponse<Void> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request) {

        return userService.updateProfile(authentication.getName(), request);
    }
}