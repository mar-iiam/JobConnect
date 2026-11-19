package com.example.jobconnect.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "JWT returned after a successful login")
public record JwtResponse(

        @Schema(
                description = "Bearer token. Paste it into the Authorize button in Swagger UI to call protected endpoints.",
                example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhaG1lZCJ9.signature"
        )
        String token
) {}