package com.example.jobconnect.services;

import com.example.jobconnect.dto.*;
import com.example.jobconnect.entity.Employer;
import com.example.jobconnect.entity.EmploymentType;
import com.example.jobconnect.entity.Job;
import com.example.jobconnect.exception.ApiException;
import com.example.jobconnect.exception.JobNotFoundException;
import com.example.jobconnect.repository.JobPaginationRepo;
import com.example.jobconnect.repository.JobRepository;
import com.example.jobconnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final JobPaginationRepo jobPaginationRepo;

    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of("id", "title", "location", "salary");

    public JobResponse createJob(
            CreateJobRequest request,
            String username) {

        Employer employer = getEmployer(username);

        Job job = new Job(
                request.getTitle(),
                request.getDescription(),
                request.getSkills(),
                request.getSalary(),
                request.getLocation(),
                request.getEmploymentType()
        );

        job.setEmployer(employer);

        Job savedJob = jobRepository.save(job);

        return toResponse(savedJob, employer);
    }

    public JobResponse updateJob(
            Long jobId,
            UpdateJobRequest request,
            String username) {

        Employer employer = getEmployer(username);

        Job job = jobRepository.findWithEmployerById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        if (job.getEmployer() == null
                || !job.getEmployer().getId().equals(employer.getId())) {
            throw new AccessDeniedException(
                    "You are not allowed to update this job"
            );
        }

        // Partial update: only change the fields that were sent
        if (request.getTitle() != null) {
            job.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            job.setDescription(request.getDescription());
        }
        if (request.getSkills() != null) {
            job.setSkills(request.getSkills());
        }
        if (request.getSalary() != null) {
            job.setSalary(request.getSalary());
        }
        if (request.getLocation() != null) {
            job.setLocation(request.getLocation());
        }
        if (request.getEmploymentType() != null) {
            try {
                job.setEmploymentType(
                        EmploymentType.valueOf(
                                String.valueOf(request.getEmploymentType())
                                        .trim()
                                        .toUpperCase(Locale.ROOT)
                        )
                );
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(
                        "Invalid employmentType. Allowed values: "
                                + Arrays.toString(EmploymentType.values())
                );
            }
        }

        Job updatedJob = jobRepository.save(job);

        return toResponse(updatedJob, employer);
    }

    public void deleteJob(
            Long jobId,
            String username) {

        Employer employer = getEmployer(username);

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        if (job.getEmployer() == null
                || !job.getEmployer().getId().equals(employer.getId())) {
            throw new AccessDeniedException(
                    "You are not allowed to delete this job"
            );
        }

        jobRepository.delete(job);
    }

    public PageResponseDto<JobResponseDto> searchJobs(
            String title,
            String location,
            BigDecimal minSalary,
            BigDecimal maxSalary,
            int page,
            int size,
            String sortBy,
            String direction) {

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page must be zero or greater"
            );
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100"
            );
        }

        if (minSalary != null && minSalary.signum() < 0) {
            throw new IllegalArgumentException(
                    "Minimum salary cannot be negative"
            );
        }

        if (maxSalary != null && maxSalary.signum() < 0) {
            throw new IllegalArgumentException(
                    "Maximum salary cannot be negative"
            );
        }

        if (minSalary != null
                && maxSalary != null
                && minSalary.compareTo(maxSalary) > 0) {
            throw new IllegalArgumentException(
                    "Minimum salary cannot exceed maximum salary"
            );
        }

        title = normalize(title);
        location = normalize(location);

        String field = sortBy == null || sortBy.isBlank()
                ? "id"
                : sortBy.trim().toLowerCase(Locale.ROOT);

        if (!ALLOWED_SORT_FIELDS.contains(field)) {
            throw new IllegalArgumentException(
                    "sortBy must be one of: id, title, location, salary"
            );
        }

        String order = direction == null || direction.isBlank()
                ? "asc"
                : direction.trim().toLowerCase(Locale.ROOT);

        Sort.Direction sortDirection;

        if ("asc".equals(order)) {
            sortDirection = Sort.Direction.ASC;
        } else if ("desc".equals(order)) {
            sortDirection = Sort.Direction.DESC;
        } else {
            throw new IllegalArgumentException(
                    "Direction must be asc or desc"
            );
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, field)
        );

        Page<Job> jobs = jobPaginationRepo.searchJobs(
                title,
                location,
                minSalary,
                maxSalary,
                pageable
        );

        Page<JobResponseDto> dtoPage =
                jobs.map(JobResponseDto::fromEntity);

        return PageResponseDto.from(dtoPage);
    }

    public Long countJobsByEmployer(Long employerId) {
        if (employerId == null || employerId <= 0) {
            throw new IllegalArgumentException(
                    "Employer ID must be positive"
            );
        }

        return jobRepository.countJobsByEmployer(employerId);
    }

    // ---------- helpers ----------

    private Employer getEmployer(String username) {
        Employer employer = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(404, "AUTH_002", "User not found"))
                .getEmployer();

        if (employer == null) {
            throw new AccessDeniedException("Only employers can manage jobs");
        }

        return employer;
    }

    private JobResponse toResponse(Job job, Employer employer) {
        JobResponse response = new JobResponse();
        response.setId(job.getId());
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());
        response.setSkills(job.getSkills());
        response.setSalary(job.getSalary());
        response.setLocation(job.getLocation());
        response.setEmploymentType(job.getEmploymentType());
        response.setEmployerId(employer.getId());
        return response;
    }

    private String normalize(String value) {
        return value == null || value.isBlank()
                ? null
                : value.trim();
    }
}