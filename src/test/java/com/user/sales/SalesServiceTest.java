package com.user.sales;

import com.invoice.sales.Sales;
import com.invoice.sales.SalesCsvParser;
import com.invoice.sales.SalesRepository;
import com.invoice.sales.SalesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalesServiceTest {

    @Mock
    private SalesRepository salesRepository;

    @Mock
    private SalesCsvParser salesCsvParser;

    @InjectMocks
    private SalesService salesService;

    @BeforeEach
    void setUp() {
        // You can initialize mocks here if needed, but
        // @ExtendWith(MockitoExtension.class) will do that automatically
    }

    @Test
    void testSaveSalesData() throws Exception {
        // Prepare the mock data
        String csvContent = "user_id,location_id,product_id,amount,date\n1,101,5001,150.75,2024-12-01";
        MockMultipartFile file = new MockMultipartFile("file", "sales_data.csv", "text/csv", csvContent.getBytes());

        Sales sales = new Sales();
        sales.setUserId(1L);
        sales.setLocationId(101L);
        sales.setProductId(5001L);
        sales.setAmount(new BigDecimal("150.75"));
        sales.setDate(LocalDate.of(2024, 12, 1));

        // Mocking the SalesCsvParser to return a list of Sales
        when(salesCsvParser.parseCsv(any(InputStreamReader.class))).thenReturn(Arrays.asList(sales));

        // Mocking the SalesRepository to return the same list when saveAll is called
        when(salesRepository.saveAll(anyList())).thenReturn(Arrays.asList(sales));

        // Call the method
        salesService.saveSalesData(file);

        // Verify interactions
        verify(salesCsvParser, times(1)).parseCsv(any(InputStreamReader.class));
        verify(salesRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testSaveSalesData_shouldThrowException_whenErrorOccurs() throws Exception {
        // Prepare the mock data
        String csvContent = "user_id,location_id,product_id,amount,date\n1,101,5001,150.75,2024-12-01";
        MockMultipartFile file = new MockMultipartFile("file", "sales_data.csv", "text/csv", csvContent.getBytes());

        // Mocking the SalesCsvParser to throw an exception
        when(salesCsvParser.parseCsv(any(InputStreamReader.class)))
                .thenThrow(new RuntimeException("CSV parsing error"));

        // Call the method and assert that an exception is thrown
        Exception exception = assertThrows(Exception.class, () -> salesService.saveSalesData(file));
        assertEquals("Failed to process CSV file", exception.getMessage());
    }

    @Test
    void testGetAllSalesData() {
        // Prepare mock data
        Sales sales1 = new Sales();
        sales1.setUserId(1L);
        sales1.setLocationId(101L);
        sales1.setProductId(5001L);
        sales1.setAmount(new BigDecimal("150.75"));
        sales1.setDate(LocalDate.of(2024, 12, 1));

        Sales sales2 = new Sales();
        sales2.setUserId(2L);
        sales2.setLocationId(102L);
        sales2.setProductId(5002L);
        sales2.setAmount(new BigDecimal("200.50"));
        sales2.setDate(LocalDate.of(2024, 12, 2));

        List<Sales> salesList = Arrays.asList(sales1, sales2);

        // Mocking the SalesRepository to return a list of Sales
        when(salesRepository.findAll()).thenReturn(salesList);

        // Call the method
        List<Sales> result = salesService.getAllSalesData();

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getUserId());
        assertEquals(2L, result.get(1).getUserId());
    }
}
