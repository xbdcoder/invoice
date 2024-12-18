package com.invoice.job;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminRepositoryItemWriter implements ItemWriter<Admin> {

    private final AdminRepository adminRepository;

    // Constructor injection of AdminRepository
    public AdminRepositoryItemWriter(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    // Write method to persist the chunk of items to the database
    @Override
    public void write(Chunk<? extends Admin> chunk) throws Exception {
        List<? extends Admin> items = chunk.getItems();  // Extract the list of items from the chunk
        adminRepository.saveAll(items);  // Save the list of Admin entities to the database
    }
}
