package com.invoice.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdminRepository userRepository;

    @Retryable(maxAttempts = 5, value = Exception.class, backoff = @Backoff(delay = 5000, multiplier = 2))
    public void fetchAndSaveUsers(List<Integer> ids) {
        String apiUrl = "http://localhost:8989/api/user"; // Replace with actual API URL

        // Fetch all users from the external API
        List<Admin> users = restTemplate.getForObject(apiUrl, List.class);

        if (users != null) {
            // Filter the users based on the provided IDs
            List<Admin> filteredUsers = users.stream()
                    .filter(user -> ids.contains(Integer.parseInt(user.getId())))
                    .collect(Collectors.toList());

            // Save filtered users to the database
            filteredUsers.forEach(user -> {
                Admin dbUser = new Admin();
                dbUser.setId(user.getId());
                dbUser.setFirstName(user.getFirstName());
                dbUser.setLastName(user.getLastName());
                userRepository.save(dbUser);
            });
        } else {
            throw new RuntimeException("Failed to fetch users from the API");
        }
    }
}
