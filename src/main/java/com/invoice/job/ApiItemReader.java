package com.invoice.job;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.invoice.util.WebClientUtil;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Component
public class ApiItemReader implements ItemReader<Admin> {

    private final WebClientUtil webClientUtil;
    private List<Integer> ids;
    private Iterator<Integer> idIterator;

    private final ObjectMapper objectMapper = new ObjectMapper();  // Jackson ObjectMapper for parsing

    // Constructor to initialize the RestTemplate
    public ApiItemReader(WebClientUtil webClientUtil) {
        this.webClientUtil = webClientUtil;
        this.idIterator = (Iterator<Integer>) ids;
    }

    // Method to be called before reading starts
    public void before(StepExecution stepExecution) {
        // Retrieve JobParameters from StepExecution
        JobParameters jobParameters = stepExecution.getJobParameters();

        // Example: get the 'ids' parameter from JobParameters
        String idsParam = jobParameters.getString("ids");
        System.out.println("Request failed: " + idsParam);
        if (idsParam != null && !idsParam.isEmpty()) {
            try {
                // Parse the string into a List<Integer> using Jackson
                this.ids = objectMapper.readValue(idsParam, objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class));
                this.idIterator = ids.iterator();
            } catch (IOException e) {
                System.err.println("Error parsing IDs: " + e.getMessage());
            }
        }
    }

    @Override
    public Admin read() throws Exception {
        if (idIterator == null || !idIterator.hasNext()) {
            return null;  // Return null when all IDs have been processed
        }

        // Prepare the body for the POST request
        Integer id = idIterator.next();
        String apiUrl = "http://localhost:8989/api/user/";

        // Create the request body with the current id
        RequestBody body = new RequestBody(Arrays.asList(id));

        // Make the POST request with WebClient, retrieve the response as String
        Mono<String> responseMono = webClientUtil.makeRequest(
                "POST", // HTTP Method
                apiUrl, // URL
                null, // No query parameters
                null, // No custom headers
                body, // Body (with ids list)
                String.class // Get response as String
        );

        // Block the Mono to get the String response
        String responseBody = responseMono.block();

        // If the response is not null, deserialize it into a List of Admin
        if (responseBody != null && !responseBody.isEmpty()) {
            try {
                // Convert the response String to List<Admin> using Jackson's ObjectMapper
                List<Admin> admins = objectMapper.readValue(responseBody, new TypeReference<List<Admin>>(){});
                if (admins != null && !admins.isEmpty()) {
                    return admins.get(0);  // Return the first admin object from the list
                }
            } catch (IOException e) {
                // Handle the deserialization error
                System.err.println("Error deserializing response: " + e.getMessage());
            }
        }

        return null;  // If no admins are returned or error occurred
    }

    // POJO for the request body
    public static class RequestBody {
        private List<Integer> ids;

        public RequestBody(List<Integer> ids) {
            this.ids = ids;
        }

        public List<Integer> getIds() {
            return ids;
        }

        public void setIds(List<Integer> ids) {
            this.ids = ids;
        }
    }
    }