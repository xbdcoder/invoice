package com.invoice.auth;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDTO(user);
    }

    public User saveUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        return userRepository.save(user);
    }

//    public List<User> all() {
//        return userRepository.findAll();
//    }
}
