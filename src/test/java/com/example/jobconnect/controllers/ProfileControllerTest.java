package com.example.jobconnect.controllers;

import com.example.jobconnect.controller.ProfileController;
import com.example.jobconnect.dto.UserProfileResponse;
import com.example.jobconnect.services.CustomUserDetailsService;
import com.example.jobconnect.services.UserService;
import com.example.jobconnect.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileController.class)
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private JwtUtil jwtUtil;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(username = "john_doe", roles = "USER")
    void getProfile_ShouldReturnProfile() throws Exception {

        UserProfileResponse response = UserProfileResponse.builder()
                .fullName("Mariiam Amr")
                .email("john@gmail.com")
                .username("john_doe")
                .roles(Set.of("JOB_SEEKER"))
                .skills("Java, Spring Boot")
                .experienceLevel("MID")
                .build();

        when(userService.getCurrentUserProfile("john_doe"))
                .thenReturn(response);

        mockMvc.perform(get("/api/profile/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john_doe"))
                .andExpect(jsonPath("$.fullName").value("Mariiam Amr"))
                .andExpect(jsonPath("$.email").value("john@gmail.com"));
    }
}