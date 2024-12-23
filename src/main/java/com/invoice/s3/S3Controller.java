package com.invoice.s3;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/s3")
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    /**
     * Endpoint to upload XML data from the frontend to S3.
     *
     * @param xmlData XML content as String
     * @return ResponseEntity with status and message
     */
    @PostMapping(value = "/upload", consumes = "application/xml")
    public ResponseEntity<String> uploadXmlToS3(@RequestBody String xmlData) {
        String bucketName = "test-bucket"; // Replace with your bucket name
        // Get the current time with seconds
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String currentTime = now.format(formatter);

        // Create the file name with current time prepended
        String fileName = currentTime + "_uploaded-data.xml"; // Customize file name logic if needed

        System.out.println("File URL: " + xmlData);
        try {
            String fileUrl = s3Service.uploadFileToS3(bucketName, fileName, xmlData);
            System.out.println("File URL: " + fileUrl);
            return ResponseEntity.ok("XML data uploaded successfully.");
        } catch (S3Exception e) {
            return ResponseEntity.status(500).body("Failed to upload XML: " + e.getMessage());
        }
    }
}

