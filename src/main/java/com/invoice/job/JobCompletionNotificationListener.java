//package com.invoice.job;
//
//import com.invoice.slack.PublishingMessage;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.batch.core.JobExecution;
//import org.springframework.batch.core.JobExecutionListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//@Component("jobCompletionNotificationListener1")  // Use a unique name
//public class JobCompletionNotificationListener implements JobExecutionListener {
//
//    @Autowired
//    private PublishingMessage publishingMessage;
//
//    @Override
//    public void beforeJob(@NotNull JobExecution jobExecution) {
//        // Log before the job starts
//        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        String message = time + ": Job is about to start...";
//        publishingMessage.publishMessage(message);
//        System.out.println(message);
//    }
//
//    @Override
//    public void afterJob(JobExecution jobExecution) {
//        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        // Log the job status after completion
//        String message = "";
//        if (jobExecution.getStatus().isUnsuccessful()) {
//            message = time + ": Job failed with status: " + jobExecution.getStatus();
//            System.out.println(message);
//        } else {
//            message = time + ": Job completed successfully with status: " + jobExecution.getStatus();
//            System.out.println(message);
//        }
//
//        publishingMessage.publishMessage(message);
//    }
//}
