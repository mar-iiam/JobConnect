
package com.example.jobconnect.repository;

import com.example.jobconnect.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface JobPaginationRepo
        extends PagingAndSortingRepository<Job, Long> {

    @EntityGraph(attributePaths = {"employer"})
    @Query(
            value = """
            SELECT j
            FROM Job j
            WHERE
                (COALESCE(:title, '') = ''
                    OR LOWER(j.title)
                    LIKE LOWER(CONCAT('%', :title, '%')))
            AND (COALESCE(:location, '') = ''
                    OR LOWER(j.location)
                    LIKE LOWER(CONCAT('%', :location, '%')))
            AND (:minSalary IS NULL OR j.salary >= :minSalary)
            AND (:maxSalary IS NULL OR j.salary <= :maxSalary)
            """,
            countQuery = """
            SELECT COUNT(j)
            FROM Job j
            WHERE
                (COALESCE(:title, '') = ''
                    OR LOWER(j.title)
                    LIKE LOWER(CONCAT('%', :title, '%')))
            AND (COALESCE(:location, '') = ''
                    OR LOWER(j.location)
                    LIKE LOWER(CONCAT('%', :location, '%')))
            AND (:minSalary IS NULL OR j.salary >= :minSalary)
            AND (:maxSalary IS NULL OR j.salary <= :maxSalary)
            """
    )
    Page<Job> searchJobs(
            @Param("title") String title,
            @Param("location") String location,
            @Param("minSalary") BigDecimal minSalary,
            @Param("maxSalary") BigDecimal maxSalary,
            Pageable pageable
    );
}
