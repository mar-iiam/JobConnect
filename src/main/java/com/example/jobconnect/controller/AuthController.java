package com.example.jobconnect.controller;

import com.example.jobconnect.audit.AuditService;
import com.example.jobconnect.dto.ApiResponse;
import com.example.jobconnect.dto.JwtResponse;
import com.example.jobconnect.dto.LoginRequest;
import com.example.jobconnect.dto.RegisterRequest;
import com.example.jobconnect.services.AuthService;
import com.example.jobconnect.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Register new users and log in to obtain a JWT")
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
    @Operation(
            summary = "Register a new user",
            description = "Creates a new account. This endpoint is public and does not require a token."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "Validation error (missing or invalid fields)",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409", description = "Email already registered",
                    content = @Content)
    })
    @SecurityRequirements
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
    @Operation(
            summary = "Log in",
            description = "Authenticates with username and password and returns a JWT. "
                    + "Use the token with the Authorize button in Swagger UI."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "Login successful, JWT returned",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "Validation error", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "Invalid username or password", content = @Content)
    })
    @SecurityRequirements
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