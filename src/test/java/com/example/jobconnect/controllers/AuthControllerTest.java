package com.example.jobconnect.controllers;


import com.example.jobconnect.Config.TestSecurityConfig;
import com.example.jobconnect.controller.AuthController;
import com.example.jobconnect.dto.ApiResponse;
import com.example.jobconnect.dto.RegisterRequest;
import com.example.jobconnect.security.JwtAuthenticationFilter;
import com.example.jobconnect.services.AuthService;
import com.example.jobconnect.util.JwtUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean

    AuthService authService;

    @MockitoBean
    AuthenticationManager authenticationManager;

    @MockitoBean
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockitoBean
    JwtUtil jwtUtil;

    @Test
    void register_ShouldReturn201() throws Exception {

        RegisterRequest request = new RegisterRequest();

        request.setFullName("Ahmed");
        request.setEmail("test@test.com");
        request.setUsername("ahmed");
        request.setPassword("123456");
        request.setRole("JOB_SEEKER");

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .statusCode(201)
                .message("User registered successfully")
                .build();

        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message")
                        .value("User registered successfully"));
    }
}
