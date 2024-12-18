package com.invoice.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batch")
public class AdminBatchController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job; // AdminBatchConfig job

    @PostMapping("/process")
    public String startBatchJob(@RequestBody List<Integer> ids) {
        try {
            // Pass the IDs as Job Parameters
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())  // Ensure uniqueness for each job run
                    .addString("ids", ids.toString())  // Pass the list of IDs to JobParameters
                    .toJobParameters();

            // Run the batch job
            jobLauncher.run(job, jobParameters);
            return "Batch job has been invoked.";
        } catch (Exception e) {
            return "Batch job failed: " + e.getMessage();
        }
    }
}
