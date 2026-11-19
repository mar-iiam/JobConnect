package com.example.jobconnect.exception;

import com.example.jobconnect.dto.ApiResponse;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import lombok.extern.slf4j.Slf4j;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j                      // <-- add this
@RestControllerAdvice
public class GlobalExceptionHandler {


    // Handle custom business exceptions
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Object>> handleApiException(ApiException ex) {
        ApiResponse<Object> response = ApiResponse.builder()
                .statusCode(ex.getStatusCode())
                .message(ex.getMessage())
                .errorCode(ex.getErrorCode())
                .data(null)
                .build();

        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }

    // Handle validation errors (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errors.putIfAbsent(err.getField(), err.getDefaultMessage()));

        ApiResponse<Object> response = ApiResponse.builder()
                .statusCode(400)
                .message("Validation failed for one or more fields")
                .errorCode("VALIDATION_001")
                .data(errors)
                .build();

        return ResponseEntity.badRequest().body(response);
    }
    // Handle all unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        log.error("Unexpected error", ex);

        ApiResponse<Object> response = ApiResponse.builder()
                .statusCode(500)
                .message("Internal server error")
                .errorCode("SYS_001")
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentials(BadCredentialsException ex) {

        ApiResponse<Object> response = ApiResponse.builder()
                .statusCode(401)
                .message("Invalid username or password")
                .errorCode("AUTH_001")
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFound(UsernameNotFoundException ex) {

        ApiResponse<Object> response = ApiResponse.builder()
                .statusCode(404)
                .message(ex.getMessage())
                .errorCode("AUTH_002")
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(AccessDeniedException ex) {

        ApiResponse<Object> response = ApiResponse.builder()
                .statusCode(403)
                .message("You do not have permission to access this resource")
                .errorCode("AUTH_003")
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(AuthenticationException ex) {

        ApiResponse<Object> response = ApiResponse.builder()
                .statusCode(401)
                .message("Authentication failed")
                .errorCode("AUTH_004")
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnreadable(HttpMessageNotReadableException ex) {
        ApiResponse<Object> response = ApiResponse.builder()
                .statusCode(400)
                .message("Request body is missing, malformed, or has an invalid value (for example an unknown employmentType)")
                .errorCode("VALIDATION_002")
                .data(null)
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ApiResponse<Object> response = ApiResponse.builder()
                .statusCode(400)
                .message("Invalid value for parameter '" + ex.getName() + "'")
                .errorCode("VALIDATION_003")
                .data(null)
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
        ApiResponse<Object> response = ApiResponse.builder()
                .statusCode(400)
                .message(ex.getMessage())
                .errorCode("VALIDATION_004")
                .data(null)
                .build();
        return ResponseEntity.badRequest().body(response);
    }
}
