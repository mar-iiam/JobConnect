package com.example.jobconnect.dto;

import com.example.jobconnect.entity.EmploymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Payload for updating a job. Send only the fields you want to change.")
public class UpdateJobRequest {

    @Schema(description = "New job title", example = "Lead Java Developer", nullable = true)
    private String title;

    @Schema(description = "New job description", example = "Lead a team building Spring Boot microservices.", nullable = true)
    private String description;

    @Schema(description = "Comma-separated list of required skills", example = "Java, Spring Boot, SQL, Docker", nullable = true)
    private String skills;

    @Schema(description = "New salary amount", example = "9000", nullable = true)
    private BigDecimal salary;

    @Schema(description = "New job location", example = "Cairo, Egypt", nullable = true)
    private String location;

    @Schema(description = "Type of employment", example = "FULL_TIME", nullable = true)
    private EmploymentType employmentType;
}