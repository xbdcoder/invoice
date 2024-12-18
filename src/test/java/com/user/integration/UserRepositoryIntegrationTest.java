//package com.user.integration;
//
//import com.example.demo.entity.User;
//import com.example.demo.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@ActiveProfiles("test") // Use application-test.properties
//public class UserRepositoryIntegrationTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    void whenSaveUser_thenUserIsPersisted() {
//        // Arrange
//        User user = new User();
//        user.setName("John Doe");
//        user.setEmail("john.doe@example.com");
//
//        // Act
//        User savedUser = userRepository.save(user);
//
//        // Assert
//        assertThat(savedUser).isNotNull();
//        assertThat(savedUser.getId()).isNotNull();
//        assertThat(savedUser.getName()).isEqualTo("John Doe");
//    }
//}
