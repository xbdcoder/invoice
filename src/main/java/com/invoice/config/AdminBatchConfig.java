package com.invoice.config;

import com.invoice.job.*;
import com.invoice.util.WebClientUtil;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class AdminBatchConfig {

//    @Autowired
//    private JobCompletionNotificationListener jobCompletionNotificationListener;

    // Custom ItemReader to fetch data from the API
//    @Bean(name = "adminReader")
    public ApiItemReader reader(WebClientUtil restTemplate) {
        return new ApiItemReader(restTemplate);  // ApiItemReader should implement ItemReader<Admin>
    }

    // ItemProcessor to process Admin objects if necessary
    @Bean
    public AdminItemProcessor processor() {
        return new AdminItemProcessor();
    }

    // ItemWriter to insert data into the database via AdminRepository
    @Bean
    public AdminRepositoryItemWriter writer(AdminRepository adminRepository) {
        return new AdminRepositoryItemWriter(adminRepository);
    }

    // Define Step to process the data and write to the database manually using StepBuilder
    @Bean
    public Step adminStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                          ApiItemReader reader, AdminItemProcessor processor, AdminRepositoryItemWriter writer) {

        // Using StepBuilder to define the chunk and other configurations
        return new StepBuilder("adminStep", jobRepository)
                .<Admin, Admin>chunk(10, transactionManager)  // Chunk size (number of items per transaction)
                .reader(reader)  // ItemReader to read data (API/DB)
                .processor(processor)  // ItemProcessor to process data (if needed)
                .writer(writer)  // ItemWriter to save data to the database
                .build();
    }

    // Job configuration to launch the job
    @Bean
    public Job adminJob(JobRepository jobRepository, Step adminStep) {
        return new JobBuilder("adminJob", jobRepository)
//                .listener(jobCompletionNotificationListener)  // Add the listener to the job
                .start(adminStep)
                .build();
    }

    // CommandLineRunner to launch the job when the application starts
    @Bean
    public CommandLineRunner runJob(JobLauncher jobLauncher, Job adminJob) {
        return args -> {
            try {
                JobParameters jobParameters = new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis())  // Unique parameter to avoid reruns
                        .toJobParameters();
                jobLauncher.run(adminJob, jobParameters);
                System.out.println("Batch job started.");
            } catch (JobExecutionException e) {
                System.err.println("Job execution failed: " + e.getMessage());
            }
        };
    }

    // PlatformTransactionManager bean for handling transactions
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();  // Use a real transaction manager in production
    }
}
