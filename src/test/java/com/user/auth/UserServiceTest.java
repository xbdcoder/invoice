//package com.user.auth;
//
//import com.invoice.auth.User;
//import com.invoice.auth.UserRepository;
//import com.invoice.auth.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private UserService userService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testProcessAndSaveUser() {
//        // Arrange
//        User inputUser = new User();
//        inputUser.setName(" John Doe ");
//        inputUser.setEmail("john.doe@example.com");
//
//        User savedUser = new User();
//        savedUser.setName("John Doe");
//        savedUser.setEmail("john.doe@example.com");
//
//        when(userRepository.save(any(User.class))).thenReturn(savedUser);
//
//        // Act
//        User result = userService.processAndSaveUser(inputUser);
//
//        // Assert
//        assertEquals("John Doe", result.getName());
//        assertEquals("john.doe@example.com", result.getEmail());
//        verify(userRepository, times(1)).save(any(User.class));
//    }
//}
