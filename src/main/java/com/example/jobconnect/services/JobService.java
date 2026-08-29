package com.example.jobconnect.services;

import com.example.jobconnect.dto.CreateJobRequest;
import com.example.jobconnect.dto.JobResponse;
import com.example.jobconnect.entity.Job;
import com.example.jobconnect.entity.User;
import com.example.jobconnect.repository.JobRepository;
import com.example.jobconnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobResponse createJob(
            CreateJobRequest request,
            String username) {

        User employer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

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
}