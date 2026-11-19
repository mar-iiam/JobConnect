package com.example.jobconnect.controller;

import com.example.jobconnect.dto.*;
import com.example.jobconnect.entity.Job;
import com.example.jobconnect.entity.JobPageResponse;
import com.example.jobconnect.services.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.Map;

@Tag(name = "Jobs", description = "Create, update, delete and search job postings")
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @Operation(
            summary = "Create a new job",
            description = "Creates a job posting owned by the authenticated user. Employer role required."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Job created",
                    content = @Content(schema = @Schema(implementation = JobResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Only employers can create jobs", content = @Content)
    })
    @PostMapping
    public ResponseEntity<JobResponse> createJob(
            @Valid @RequestBody CreateJobRequest request,
            Authentication authentication) {

        JobResponse response = jobService.createJob(request, authentication.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Update a job",
            description = "Updates an existing job. Only the employer who owns the job can update it."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Job updated",
                    content = @Content(schema = @Schema(implementation = JobResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token", content = @Content),
            @ApiResponse(responseCode = "403", description = "You do not own this job", content = @Content),
            @ApiResponse(responseCode = "404", description = "Job not found", content = @Content)
    })
    @PutMapping("/{jobId}")
    public ResponseEntity<JobResponse> updateJob(
            @Parameter(description = "ID of the job to update", example = "1")
            @PathVariable Long jobId,
            @RequestBody UpdateJobRequest request,
            Authentication authentication) throws AccessDeniedException {

        String username = authentication.getName();

        JobResponse response = jobService.updateJob(jobId, request, username);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete a job",
            description = "Deletes a job. Only the employer who owns the job can delete it."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Job deleted"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token", content = @Content),
            @ApiResponse(responseCode = "403", description = "You do not own this job", content = @Content),
            @ApiResponse(responseCode = "404", description = "Job not found", content = @Content)
    })
    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> deleteJob(
            @Parameter(description = "ID of the job to delete", example = "1")
            @PathVariable Long jobId,
            Authentication authentication) throws AccessDeniedException {

        String username = authentication.getName();

        jobService.deleteJob(jobId, username);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Search jobs",
            description = "Returns a paginated, sortable list of jobs. All filters are optional and can be combined."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Jobs retrieved",
                    content = @Content(schema = @Schema(implementation = PageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameter, for example an unknown sort field",
                    content = @Content(schema = @Schema(example = "{\"error\": \"Invalid sort field\"}"))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token", content = @Content)
    })
    @GetMapping("/search")
    public ResponseEntity<?> searchJobs(
            @Parameter(description = "Filter by job title (partial match)", example = "Java Developer")
            @RequestParam(required = false) String title,
            @Parameter(description = "Filter by location", example = "Cairo")
            @RequestParam(required = false) String location,
            @Parameter(description = "Minimum salary", example = "3000")
            @RequestParam(required = false) BigDecimal minSalary,
            @Parameter(description = "Maximum salary", example = "10000")
            @RequestParam(required = false) BigDecimal maxSalary,
            @Parameter(description = "Page number, starting at 0", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by", example = "id")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", schema = @Schema(allowableValues = {"asc", "desc"}))
            @RequestParam(defaultValue = "asc") String direction) {

        try {
            PageResponseDto<JobResponseDto> result =
                    jobService.searchJobs(title, location, minSalary, maxSalary,
                            page, size, sortBy, direction);

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @Operation(
            summary = "Count jobs by employer",
            description = "Returns how many jobs a given employer has posted."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Jobs retrieved",
                    content = @Content(schema = @Schema(implementation = JobPageResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token", content = @Content)
    })
    @GetMapping("/employer/{employerId}/count")
    public Long countJobsByEmployer(
            @Parameter(description = "ID of the employer", example = "3")
            @PathVariable Long employerId) {
        return jobService.countJobsByEmployer(employerId);
    }
}