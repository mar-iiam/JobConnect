package com.example.jobconnect.services;


import com.example.jobconnect.dto.RegisterRequest;
import com.example.jobconnect.entity.Role;
import com.example.jobconnect.entity.User;
import com.example.jobconnect.exception.ApiException;
import com.example.jobconnect.repository.RoleRepository;
import com.example.jobconnect.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    AuthService authService;

    RegisterRequest request;

    @BeforeEach
    void setUp() {

        request = new RegisterRequest();

        request.setFullName("Ahmed");
        request.setEmail("ahmed@test.com");
        request.setUsername("ahmed");
        request.setPassword("123456");
        request.setRole("JOB_SEEKER");
        request.setSkills("Java");
        request.setExperienceLevel("Junior");
    }

    @Test
    void register_ShouldRegisterUserSuccessfully() {

        Role role = Role.builder()
                .name("JOB_SEEKER")
                .build();

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(userRepository.existsByUsername(request.getUsername()))
                .thenReturn(false);

        when(roleRepository.findByName("JOB_SEEKER"))
                .thenReturn(Optional.of(role));

        when(passwordEncoder.encode("123456"))
                .thenReturn("encodedPassword");

        authService.register(request);

        verify(userRepository, times(1))
                .save(any(User.class));
    }

    @Test
    void register_ShouldThrowException_WhenEmailExists() {

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        ApiException exception = assertThrows(
                ApiException.class,
                () -> authService.register(request)
        );

        assertEquals("Email already exists", exception.getMessage());

        verify(userRepository, never()).save(any());
    }

    @Test
    void register_ShouldThrowException_WhenUsernameExists() {

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(userRepository.existsByUsername(request.getUsername()))
                .thenReturn(true);

        ApiException exception = assertThrows(
                ApiException.class,
                () -> authService.register(request)
        );

        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void register_ShouldThrowException_WhenRoleInvalid() {

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(userRepository.existsByUsername(request.getUsername()))
                .thenReturn(false);

        when(roleRepository.findByName(request.getRole()))
                .thenReturn(Optional.empty());

        ApiException exception = assertThrows(
                ApiException.class,
                () -> authService.register(request)
        );

        assertEquals("Invalid role provided", exception.getMessage());
    }
}
