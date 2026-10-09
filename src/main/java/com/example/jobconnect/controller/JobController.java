package com.example.jobconnect.controller;

import com.example.jobconnect.dto.*;
import com.example.jobconnect.entity.Job;

import com.example.jobconnect.services.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    public ResponseEntity<JobResponse> createJob(
            @Valid @RequestBody CreateJobRequest request,
            Authentication authentication) {

        JobResponse response = jobService.createJob(
                request,
                authentication.getName()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<JobResponse> updateJob(
            @PathVariable Long jobId,
            @RequestBody UpdateJobRequest request,
            Authentication authentication) throws AccessDeniedException {

        String username = authentication.getName();

        JobResponse response = jobService.updateJob(
                jobId,
                request,
                username
        );

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable Long jobId,
            Authentication authentication) throws AccessDeniedException {

        String username = authentication.getName();

        jobService.deleteJob(
                jobId,
                username
        );

        return ResponseEntity.noContent().build();
    }
    @GetMapping("/search")
    public ResponseEntity<?> searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) BigDecimal minSalary,
            @RequestParam(required = false) BigDecimal maxSalary,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        try {
            PageResponseDto<JobResponseDto> result =
                    jobService.searchJobs(
                            title, location,
                            minSalary, maxSalary,
                            page, size, sortBy, direction
                    );

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}