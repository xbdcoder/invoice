package com.invoice.auth;

public class UserUtil {
    public static User formatUser(User user) {
        // Example: Convert name to uppercase for demonstration
        user.setName(user.getName().toUpperCase());
        return user;
    }
}