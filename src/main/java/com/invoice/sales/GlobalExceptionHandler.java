package com.invoice.sales;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CsvValidationException.class)
    public ResponseEntity<Map<String, Object>> handleCsvValidationExceptions(CsvValidationException ex) {
        // Prepare the response body with status and message
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", ex.getMessage());

        // Return the error response with BAD_REQUEST status
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}