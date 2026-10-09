
package com.example.jobconnect.services;

import com.example.jobconnect.dto.*;
import com.example.jobconnect.entity.Employer;
import com.example.jobconnect.entity.EmploymentType;
import com.example.jobconnect.entity.Job;
import com.example.jobconnect.repository.JobPaginationRepo;
import com.example.jobconnect.repository.JobRepository;
import com.example.jobconnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
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

        Employer employer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getEmployer();

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

        JobResponse response = new JobResponse();
        response.setId(savedJob.getId());
        response.setTitle(savedJob.getTitle());
        response.setDescription(savedJob.getDescription());
        response.setSkills(savedJob.getSkills());
        response.setSalary(savedJob.getSalary());
        response.setLocation(savedJob.getLocation());
        response.setEmploymentType(savedJob.getEmploymentType());
        response.setEmployerId(employer.getId());

        return response;
    }

    public JobResponse updateJob(
            Long jobId,
            UpdateJobRequest request,
            String username) throws AccessDeniedException {

        Employer employer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getEmployer();

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (job.getEmployer() == null
                || !job.getEmployer().getId().equals(employer.getId())) {
            throw new AccessDeniedException(
                    "You are not allowed to update this job"
            );
        }

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setSkills(request.getSkills());
        job.setSalary(BigDecimal.valueOf(request.getSalary()));
        job.setLocation(request.getLocation());
        job.setEmploymentType(
                EmploymentType.valueOf(request.getEmploymentType())
        );

        Job updatedJob = jobRepository.save(job);

        JobResponse response = new JobResponse();
        response.setId(updatedJob.getId());
        response.setTitle(updatedJob.getTitle());
        response.setDescription(updatedJob.getDescription());
        response.setSkills(updatedJob.getSkills());
        response.setSalary(updatedJob.getSalary());
        response.setLocation(updatedJob.getLocation());
        response.setEmploymentType(updatedJob.getEmploymentType());
        response.setEmployerId(employer.getId());

        return response;
    }

    public void deleteJob(
            Long jobId,
            String username) throws AccessDeniedException {

        Employer employer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getEmployer();

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

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

        // Call the injected repository instance, not the interface statically.
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

    private String normalize(String value) {
        return value == null || value.isBlank()
                ? null
                : value.trim();
    }
}
