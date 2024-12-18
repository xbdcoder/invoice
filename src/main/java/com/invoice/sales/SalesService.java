package com.invoice.sales;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.List;

@Service
public class SalesService {

    private final SalesRepository salesRepository;
    private final SalesCsvParser salesCsvParser;

    public SalesService(SalesRepository salesRepository, SalesCsvParser salesCsvParser) {
        this.salesRepository = salesRepository;
        this.salesCsvParser = salesCsvParser;
    }

    public void saveSalesData(MultipartFile file) throws Exception {
        try (InputStreamReader reader = new InputStreamReader(file.getInputStream())) {
            List<Sales> salesList = salesCsvParser.parseCsv(reader);
            salesRepository.saveAll(salesList);
        } catch (Exception e) {
            throw new CsvValidationException("Failed to process CSV file:" + e.getMessage());
        }
    }

    public List<Sales> getAllSalesData() {
        return salesRepository.findAll();
    }
}
