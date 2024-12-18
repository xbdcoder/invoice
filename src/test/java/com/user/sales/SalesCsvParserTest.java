package com.user.sales;

import com.invoice.sales.CsvValidationException;
import com.invoice.sales.Sales;
import com.invoice.sales.SalesCsvParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SalesCsvParserTest {

    @InjectMocks
    private SalesCsvParser salesCsvParser; // The class to be tested

    @Mock
    private InputStreamReader reader; // Mocked InputStreamReader to simulate input

    @Mock
    private BufferedReader bufferedReader; // Mocked BufferedReader

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testParseCsv_ValidData() throws Exception {
        // Mock the behavior of bufferedReader
        when(bufferedReader.readLine()).thenReturn("userId,locationId,productId,amount,date")
                .thenReturn("123,456,789,100.50,2024-12-04")
                .thenReturn(null); // End of file

        // Parse the CSV
        List<Sales> salesList = salesCsvParser.parseCsv(reader);

        // Verify the correct parsing
        assertEquals(1, salesList.size()); // One valid record
        Sales sales = salesList.get(0);
        assertEquals(123L, sales.getUserId());
        assertEquals(456L, sales.getLocationId());
        assertEquals(789L, sales.getProductId());
        assertEquals(new BigDecimal("100.50"), sales.getAmount());
        assertEquals("2024-12-04", sales.getDate().toString());
    }

    @Test
    void testParseCsv_InvalidUserId() throws Exception {
        // Mock invalid userId (non-numeric)
        when(bufferedReader.readLine()).thenReturn("userId,locationId,productId,amount,date")
                .thenReturn("abc,456,789,100.50,2024-12-04")
                .thenReturn(null);

        // Parse the CSV
        Exception exception = assertThrows(CsvValidationException.class, () -> salesCsvParser.parseCsv(reader));

        // Verify the exception and error message
        String expectedMessage = "Validation failed for the following errors: Invalid User ID at line: abc";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void testParseCsv_InvalidAmount() throws Exception {
        // Mock invalid amount (negative value)
        when(bufferedReader.readLine()).thenReturn("userId,locationId,productId,amount,date")
                .thenReturn("123,456,789,-10.00,2024-12-04")
                .thenReturn(null);

        // Parse the CSV
        Exception exception = assertThrows(CsvValidationException.class, () -> salesCsvParser.parseCsv(reader));

        // Verify the exception and error message
        String expectedMessage = "Validation failed for the following errors: Invalid Amount (should be greater than 0) at line: 123,456,789,-10.00,2024-12-04";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void testParseCsv_InvalidDate() throws Exception {
        // Mock invalid date format
        when(bufferedReader.readLine()).thenReturn("userId,locationId,productId,amount,date")
                .thenReturn("123,456,789,100.50,2024-12-01")
                .thenReturn(null);

        // Parse the CSV
        Exception exception = assertThrows(CsvValidationException.class, () -> salesCsvParser.parseCsv(reader));

        // Verify the exception and error message
        String expectedMessage = "Validation failed for the following errors: Invalid Date at line: 123,456,789,100.50,2024-12-01";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void testParseCsv_EmptyFile() throws Exception {
        // Mock empty file (no data, only header)
        when(bufferedReader.readLine()).thenReturn("userId,locationId,productId,amount,date")
                .thenReturn(null); // End of file immediately after header

        // Parse the CSV
        List<Sales> salesList = salesCsvParser.parseCsv(reader);

        // Verify no data is parsed
        assertTrue(salesList.isEmpty());
    }

    @Test
    void testParseCsv_MultipleErrors() throws Exception {
        // Mock multiple invalid fields (userId, amount, and date)
        when(bufferedReader.readLine()).thenReturn("userId,locationId,productId,amount,date")
                .thenReturn("abc,456,789,-10.00,2024-13-32")
                .thenReturn(null);

        // Parse the CSV
        Exception exception = assertThrows(CsvValidationException.class, () -> salesCsvParser.parseCsv(reader));

        // Verify the exception and error message
        String expectedMessage = "Validation failed for the following errors: Invalid User ID at line: abc, Invalid Amount (should be greater than 0) at line: abc,456,789,-10.00,2024-13-32, Invalid Date at line: abc,456,789,-10.00,2024-13-32";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    // Test for handling of IOException (simulate an IO exception in reading)
    @Test
    void testParseCsv_IOError() throws Exception {
        when(bufferedReader.readLine()).thenThrow(new IOException("File read error"));

        Exception exception = assertThrows(IOException.class, () -> salesCsvParser.parseCsv(reader));

        // Verify the exception is an IOException
        assertTrue(exception.getMessage().contains("File read error"));
    }
}
