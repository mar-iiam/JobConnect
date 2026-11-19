package com.example.jobconnect.dto;

import com.example.jobconnect.entity.EmploymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Payload for creating a new job posting")
public class CreateJobRequest {

    @Schema(description = "Job title", example = "Senior Java Developer")
    @NotBlank(message = "Title is required")
    private String title;

    @Schema(description = "Detailed job description",
            example = "Design, build and maintain Spring Boot microservices.")
    @NotBlank(message = "Description is required")
    private String description;

    @Schema(description = "Comma-separated list of required skills",
            example = "Java, Spring Boot, SQL, Docker")
    @NotBlank(message = "Skills are required")
    private String skills;

    @Schema(description = "Salary amount, cannot be negative", example = "8000", minimum = "0")
    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.0", message = "Salary cannot be negative")
    private BigDecimal salary;

    @Schema(description = "Job location", example = "Cairo, Egypt")
    @NotBlank(message = "Location is required")
    private String location;

    @Schema(description = "Type of employment", example = "FULL_TIME")
    @NotNull(message = "Employment type is required")
    private EmploymentType employmentType;
}