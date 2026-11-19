package com.example.jobconnect.entity;


import com.example.jobconnect.dto.JobResponseDto;
import com.example.jobconnect.dto.PageResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "JobPageResponse", description = "Paginated list of jobs")
public abstract class JobPageResponse extends PageResponseDto<JobResponseDto> {

    private JobPageResponse() {
        super(null, 0, 0, 0, 0, false, false);
    }
}