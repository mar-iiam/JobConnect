package com.example.jobconnect.controller;


import com.example.jobconnect.Mongo.AppLog;
import com.example.jobconnect.Mongo.AppLogRepository;
import com.example.jobconnect.entity.User;
import com.example.jobconnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class DbTestController {

    private final UserRepository userRepository;
    private final AppLogRepository appLogRepository;

    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        appLogRepository.save(AppLog.builder()
                .message("Created new user: " + user.getFullName())
                .createdAt(LocalDateTime.now())
                .build());
        return savedUser;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/logs")
    public List<AppLog> getLogs() {
        return appLogRepository.findAll();
    }
}
