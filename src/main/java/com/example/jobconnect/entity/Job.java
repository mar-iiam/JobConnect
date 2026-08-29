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

    public Job(
            String title,
            String description,
            String skills,
            BigDecimal salary,
            String location) {

        this.title = title;
        this.description = description;
        this.skills = skills;
        this.salary = salary;
        this.location = location;
    }


}