package com.example.jobconnect.dto;



import com.example.jobconnect.entity.EmploymentType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class JobResponse {

    private Long id;
    private String title;
    private String description;
    private String skills;
    private BigDecimal salary;
    private String location;
    private EmploymentType employmentType;
    private Long employerId;
}