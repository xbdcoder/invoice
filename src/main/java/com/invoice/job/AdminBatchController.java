package com.invoice.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/batch")
public class AdminBatchController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job; // AdminBatchConfig job

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private ObjectMapper objectMapper;  // Ensure Jackson ObjectMapper is available

    private final AdminService adminService;

    // Constructor injection for AdminService
    public AdminBatchController(AdminService adminService) {
        this.adminService = adminService;
    }

    // Configure ThreadPoolTaskExecutor inside the controller
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        String rand = String.valueOf(Math.random()).substring(2, 8);
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);  // Minimum number of threads
        executor.setMaxPoolSize(10);  // Maximum number of threads
        executor.setQueueCapacity(25);  // Queue size for tasks waiting for an available thread
        executor.setThreadNamePrefix("batch-job-" + rand + "-");  // Add random value to thread name prefix
        executor.initialize();
        return executor;
    }

    @PostMapping("/fetch")
    public void fetchAdmins(@RequestBody List<Integer> ids) {
        adminService.fetchAndSaveUsers(ids);
    }
    @PostMapping("/process")
    public String startBatchJob(@RequestBody List<Integer> ids) {
        try {
            // Iterating 100 times using IntStream
            IntStream.range(0, 2).forEach(i -> {
                // Do something for each iteration
                System.out.println("Iteration: " + i);
                // Pass the IDs as Job Parameters
                JobParameters jobParameters = new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis())  // Ensure uniqueness for each job run
                        .addString("ids", ids.toString())  // Pass the list of IDs to JobParameters
                        .toJobParameters();

                // Create a task that runs the batch job
                Runnable task = () -> {
                    try {
                        // Run the batch job asynchronously using the jobLauncher
                        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
                        // Optionally log or handle the job execution results here
                        System.out.println("Job executed with status: " + jobExecution.getStatus());
                    } catch (Exception e) {
                        // Handle any exceptions that might occur during job execution
                        System.err.println("Job execution failed: " + e.getMessage());
                    }
                };

                // Submit the task to the thread pool for execution
                threadPoolTaskExecutor().submit(task);
            });
            return "Batch job has been invoked and is running in the background.";
        } catch (Exception e) {
            return "Batch job failed: " + e.getMessage();
        }
    }
}
