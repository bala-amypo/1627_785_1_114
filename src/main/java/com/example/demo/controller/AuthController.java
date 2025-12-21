package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

@RestController
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // POST /auth/register
    @PostMapping("/auth/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    // POST /auth/login
    @PostMapping("/auth/login")
    public String login(@RequestBody User user) {

        User existingUser = userService.findByEmail(user.getEmail());

        // Simple password check (no encoder)
        if (!existingUser.getPassword().equals(user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return "Login successful";
    }
}