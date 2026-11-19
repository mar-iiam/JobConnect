package com.example.jobconnect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard response wrapper used by register and profile update")
public class ApiResponse<T> {

    @Schema(description = "HTTP status code of the result", example = "200")
    private Integer statusCode;

    @Schema(description = "Human-readable result message", example = "Profile updated successfully")
    private String message;

    @Schema(description = "Machine-readable error code, present only when the request failed",
            example = "EMAIL_ALREADY_EXISTS", nullable = true)
    private String errorCode;

    @Schema(description = "Response payload. Empty for endpoints that return no data.", nullable = true)
    private T data;
}