package com.invoice.s3;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.*;

import java.nio.file.Path;

@Service
public class S3Service {

    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFileToS3(String bucketName, String key, String xmlData) {
        try {

            checkAndCreateBucket(bucketName);

            // Upload the XML data to the specified bucket with the specified key
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .contentType("application/xml")
                            .build(),
                    RequestBody.fromString(xmlData)
            );

            // Generate the URL of the uploaded file
            return generateS3Url(bucketName, key);
        } catch (S3Exception e) {
            // Handle AWS S3-specific exceptions (e.g., invalid bucket, access denied)
            System.err.println("S3 error: " + e.awsErrorDetails().errorMessage());
            return "S3 error: " + e.awsErrorDetails().errorMessage();
        } catch (Exception e) {
            // Handle other exceptions (e.g., network issues, unexpected errors)
            System.err.println("Error uploading file: " + e.getMessage());
            return "Error uploading file: " + e.getMessage();
        }
    }

    private void checkAndCreateBucket(String bucketName) {
        try {
            // Check if the bucket exists
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
        } catch (NoSuchBucketException e) {
            // If the bucket doesn't exist, create it
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.createBucket(createBucketRequest);
            System.out.println("Bucket created: " + bucketName);
        } catch (S3Exception e) {
            // Handle other S3 errors
            System.err.println("Error checking or creating the bucket: " + e.awsErrorDetails().errorMessage());
        }
    }

    private String generateS3Url(String bucketName, String key) {
        // LocalStack S3 endpoint URL format
        String endpoint = "http://localhost:4566";  // LocalStack S3 endpoint
        return String.format("%s/%s/%s", endpoint, bucketName, key);
    }
}
