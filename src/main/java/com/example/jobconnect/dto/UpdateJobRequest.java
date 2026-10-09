package com.example.jobconnect.dto;

import lombok.Data;

@Data
public class UpdateJobRequest {

    private String title;
    private String description;
    private String skills;
    private Double salary;
    private String location;
    private String employmentType;
    private int Employer_ID;
}