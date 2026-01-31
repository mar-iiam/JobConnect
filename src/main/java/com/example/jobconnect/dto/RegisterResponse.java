package com.example.jobconnect.dto;

import lombok.Data;

@Data
public class RegisterResponse {
    private Integer statusCode;
    private String message;
    private String errorCode;
}
