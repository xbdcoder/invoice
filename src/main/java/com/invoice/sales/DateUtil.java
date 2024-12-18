package com.invoice.sales;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {

    // Utility method to parse date from String to LocalDate
    public static LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE); // Parse using ISO_DATE format (yyyy-MM-dd)
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + dateString);
        }
    }
}
