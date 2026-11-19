package com.example.jobconnect.exception;


public class JobNotFoundException extends ApiException {

    public JobNotFoundException(Long jobId) {
        super(404, "JOB_001", "Job with id " + jobId + " was not found");
    }
}