package com.example.jobconnect.dto;

import com.example.jobconnect.entity.EmploymentType;
import com.example.jobconnect.entity.Job;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class JobResponseDto {

    private Long id;
    private String title;
    private String description;
    private String skills;
    private BigDecimal salary;
    private String location;
    private EmploymentType employmentType;
    private String employerName;


    public static JobResponseDto fromEntity(Job job) {
        JobResponseDto dto = new JobResponseDto();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setSkills(job.getSkills());
        dto.setSalary(job.getSalary());
        dto.setLocation(job.getLocation());
        dto.setEmploymentType(job.getEmploymentType());

        if (job.getEmployer() != null) {
            dto.setEmployerName(String.valueOf(job.getEmployer().getId()));
        }

        return dto;
    }
}