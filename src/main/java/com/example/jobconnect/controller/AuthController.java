package com.example.jobconnect.controller;

import com.example.jobconnect.audit.AuditService;
import com.example.jobconnect.dto.ApiResponse;
import com.example.jobconnect.dto.JwtResponse;
import com.example.jobconnect.dto.LoginRequest;
import com.example.jobconnect.dto.RegisterRequest;
import com.example.jobconnect.services.AuthService;
import com.example.jobconnect.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuditService auditService;

    public AuthController(AuthService authService,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil, AuditService auditService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.auditService = auditService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
            @Valid @RequestBody RegisterRequest request,  HttpServletRequest httpRequest) {

        ApiResponse<Void> response = authService.register(request);

        auditService.logRegistration(
                request.getEmail(), httpRequest.getRemoteAddr()

        );
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest request,  HttpServletRequest httpRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        auditService.logLogin(

                request.username(),
                httpRequest.getRemoteAddr()
        );

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String token = jwtUtil.generateToken(authentication.getName(), roles);

        return ResponseEntity.ok(new JwtResponse(token));
    }
}