package com.example.jobconnect.exception;


import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final Integer statusCode;
    private final String errorCode;

    public ApiException(Integer statusCode, String message, String errorCode) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }
}
