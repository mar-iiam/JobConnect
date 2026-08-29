package com.example.jobconnect.entity;



import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String skills;

    private BigDecimal salary;

    private String location;

    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    @ManyToOne
    @JoinColumn(name = "employer_id", nullable = false)
    private User employer;

    public Job(
            String title,
            String description,
            String skills,
            BigDecimal salary,
            String location,
            EmploymentType employmentType) {

        this.title = title;
        this.description = description;
        this.skills = skills;
        this.salary = salary;
        this.location = location;
        this.employmentType = employmentType;
    }


}