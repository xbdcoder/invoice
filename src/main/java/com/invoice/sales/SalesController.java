package com.invoice.sales;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping()
    public ResponseEntity<List<Sales>> getAllSales() {
        List<Sales> salesList = salesService.getAllSalesData();
        return ResponseEntity.ok(salesList);
    }

    /**
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadSalesData(@Valid @RequestParam("file") MultipartFile file) {
        try {
            salesService.saveSalesData(file);
            return ResponseEntity.ok("Sales data uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing file: " + e.getMessage());
        }
    }
}
