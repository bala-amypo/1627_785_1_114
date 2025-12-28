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


package com.example.demo.controller;

import com.example.demo.config.JwtTokenProvider;
import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UserServiceImpl userService;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        
        User created = userService.register(user);
        String token = jwtTokenProvider.generateToken(created.getId(), created.getEmail(), created.getRole());
        
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setEmail(created.getEmail());
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        User user = userService.findByEmail(request.getEmail());
        if (user != null) {
            String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole());
            
            AuthResponse response = new AuthResponse();
            response.setToken(token);
            response.setEmail(user.getEmail());
            
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().build();
    }
}



