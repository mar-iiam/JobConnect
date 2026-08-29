package com.example.jobconnect.services;


import com.example.jobconnect.dto.UpdateProfileRequest;
import com.example.jobconnect.entity.Role;
import com.example.jobconnect.entity.User;
import com.example.jobconnect.exception.ApiException;
import com.example.jobconnect.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void getCurrentUserProfile_ShouldReturnProfile() {

        Role role = Role.builder()
                .name("JOB_SEEKER")
                .build();

        User user = User.builder()
                .username("ahmed")
                .email("ahmed@test.com")
                .fullName("Ahmed")
                .roles(Set.of(role))
                .build();


        when(userRepository.findByUsername("ahmed"))
                .thenReturn(Optional.of(user));

        var response = userService.getCurrentUserProfile("ahmed");

        assertEquals("Ahmed", response.getFullName());
        assertEquals("ahmed", response.getUsername());
    }

    @Test
    void getCurrentUserProfile_ShouldThrowException_WhenUserNotFound() {

        when(userRepository.findByUsername("ahmed"))
                .thenReturn(Optional.empty());

        assertThrows(ApiException.class,
                () -> userService.getCurrentUserProfile("ahmed"));
    }

    @Test
    void updateProfile_ShouldUpdateUserSuccessfully() {

        User user = User.builder()
                .username("ahmed")
                .fullName("Old Name")
                .email("old@test.com")
                .build();

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFullName("New Name");

        when(userRepository.findByUsername("ahmed"))
                .thenReturn(Optional.of(user));

        userService.updateProfile("ahmed", request);

        assertEquals("New Name", user.getFullName());

        verify(userRepository).save(user);
    }
}
