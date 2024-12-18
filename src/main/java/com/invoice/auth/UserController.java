package com.invoice.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping
//    public List<User> index() {
//        return userService.all();
//    }

    @PostMapping
    public User create(@Valid @RequestBody UserDTO user) {
        return userService.saveUser(user);
    }
}