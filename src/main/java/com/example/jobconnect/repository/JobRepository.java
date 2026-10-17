package com.example.jobconnect.repository;

import com.example.jobconnect.entity.Job;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    @EntityGraph(attributePaths = {"employer"})
    Optional<Job> findWithEmployerById(Long id);

    @Procedure(
            procedureName = "count_jobs_by_employer",
            outputParameterName = "p_job_count"
    )
    Long countJobsByEmployer(
            @Param("p_employer_id") Long employerId
    );
}
