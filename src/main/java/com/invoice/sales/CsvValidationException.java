package com.invoice.sales;

public class CsvValidationException extends RuntimeException {
    public CsvValidationException(String message) {
        super(message);
    }
}