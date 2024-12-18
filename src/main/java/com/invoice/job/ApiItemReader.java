package com.invoice.job;

import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.List;

@Component
public class ApiItemReader implements ItemReader<Admin> {

    private final RestTemplate restTemplate;
    private final List<Integer> ids;
    private Iterator<Integer> idIterator;

    public ApiItemReader(RestTemplate restTemplate, List<Integer> ids) {
        this.restTemplate = restTemplate;
        this.ids = ids;
        this.idIterator = ids.iterator();
    }

    @Override
    public Admin read() throws Exception {
        if (idIterator.hasNext()) {
            Integer id = idIterator.next();
            // Make the API call using RestTemplate
            String apiUrl = "http://localhost:8989/api/user/" + id;
            return restTemplate.getForObject(apiUrl, Admin.class);
        } else {
            return null;  // Return null when all IDs have been processed
        }
    }
}
