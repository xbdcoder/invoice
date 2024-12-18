package com.invoice.sales;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class SalesCsvParser {

    public List<Sales> parseCsv(InputStreamReader reader) throws IOException {
        List<Sales> salesList = new ArrayList<>();
        List<String> validationErrors = new ArrayList<>(); // List to store validation errors
        BufferedReader bufferedReader = new BufferedReader(reader);

        // Skip header
        String line = bufferedReader.readLine();

        while ((line = bufferedReader.readLine()) != null) {
            String[] fields = line.split(",");

            // Initialize a new Sales object
            Sales sales = new Sales();

            // Validate userId (should be a valid number)
            try {
                sales.setUserId(Long.parseLong(fields[0]));
            } catch (NumberFormatException e) {
                validationErrors.add("Invalid User ID at line: " + line);
            }

            // Validate locationId (should be a valid number)
            try {
                sales.setLocationId(Long.parseLong(fields[1]));
            } catch (NumberFormatException e) {
                validationErrors.add("Invalid Location ID at line: " + line);
            }

            // Validate productId (should be a valid number)
            try {
                sales.setProductId(Long.parseLong(fields[2]));
            } catch (NumberFormatException e) {
                validationErrors.add("Invalid Product ID at line: " + line);
            }

            // Validate amount (should be a valid number and greater than 0)
            try {
                BigDecimal amount = new BigDecimal(fields[3]);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    validationErrors.add("Invalid Amount (should be greater than 0) at line: " + line);
                } else {
                    sales.setAmount(amount);
                }
            } catch (NumberFormatException e) {
                validationErrors.add("Invalid Amount at line: " + line);
            }

            // Validate date (should be in the correct format)
            try {
                sales.setDate(DateUtil.parseDate(fields[4])); // Parsing the date using DateUtil
            } catch (Exception e) {
                validationErrors.add("Invalid Date at line: " + line);
            }

            // If no validation errors, add the sales object to the list
            if (validationErrors.isEmpty()) {
                salesList.add(sales);
            }
        }

        // If validation errors exist, throw an exception with all errors
        if (!validationErrors.isEmpty()) {
            throw new CsvValidationException(
                    "Validation failed for the following errors: " + String.join(", ", validationErrors));
        }

        return salesList;
    }
}
