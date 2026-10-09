package com.example.jobconnect.entity;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Entity
@Table(name = "jobs")
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String skills;

    private BigDecimal salary;

    private String location;
    private EmploymentType employmentType;
    @ManyToOne
    @JoinColumn(name = "employer_id")
    private Employer employer;

    public Job(
            String title,
            String description,
            String skills,
            BigDecimal salary,
            String location,
            EmploymentType employmentType){

        this.title = title;
        this.description = description;
        this.skills = skills;
        this.salary = salary;
        this.location = location;
        this.employmentType=employmentType;
    }



}