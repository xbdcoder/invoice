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

    @Retryable(maxAttempts = 2, value = Exception.class, backoff = @Backoff(delay = 50, multiplier = 2))
    public void fetchAndSaveUsers(List<Integer> ids) {

        // Define the URL for the API endpoint
        String apiUrl = "http://localhost:8989/api/user";

        // Create the RequestBody object with the list of IDs
        RequestBodyAPI requestBody = new RequestBodyAPI(ids);

        // Send the POST request with the request body and receive a List of Admin objects in response
        List<Admin> admins = restTemplate.postForObject(apiUrl, requestBody, List.class);

        System.out.println(admins);

        if (admins != null) {
//            // Filter the users based on the provided IDs
//            List<Admin> filteredUsers = admins.stream()
//                    .filter(user -> ids.contains(Integer.parseInt(user.getId())))
//                    .toList();
//
//            // Save filtered users to the database
//            filteredUsers.forEach(user -> {
//                Admin dbUser = new Admin();
//                dbUser.setId(user.getId());
//                dbUser.setFirstName(user.getFirstName());
//                dbUser.setLastName(user.getLastName());
//                userRepository.save(dbUser);
//            });
        } else {
            throw new RuntimeException("Failed to fetch users from the API");
        }
    }
}
