package com.invoice.job;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class AdminItemProcessor implements ItemProcessor<Admin, Admin> {

    @Override
    public Admin process(Admin person) throws Exception {
        // Optional: Add business logic (e.g., modify the data before saving)
        return person;
    }
}