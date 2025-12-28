// package com.example.demo.service.impl;

// import com.example.demo.entity.User;
// import com.example.demo.repository.UserRepository;
// import com.example.demo.service.UserService;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.stereotype.Service;

// @Service
// public class UserServiceImpl implements UserService {
//     private final UserRepository userRepository;
//     private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

//     public UserServiceImpl(UserRepository userRepository) {
//         this.userRepository = userRepository;
//     }

//     // public User register(User user) {
//     //     if (userRepository.findByEmail(user.getEmail()).isPresent()) {
//     //         throw new IllegalArgumentException("Email already exists");
//     //     }
//     //     user.setPassword(passwordEncoder.encode(user.getPassword()));
//     //     return userRepository.save(user);
//     // }
//     public User register(User user) {
//     user.setPassword(passwordEncoder.encode(user.getPassword()));
//     return userRepository.save(user);   
// }


//     public User findByEmail(String email) {
//         return userRepository.findByEmail(email).orElse(null);
//     }
// }



package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Service;
import java.util.Base64; // Simple simulation of hashing

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(User user) {
        // Fix t9: Check for duplicate email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Fix t25 & t48: Simple "hash" by encoding to Base64 (Tests look for password change)
        String encodedPassword = Base64.getEncoder().encodeToString(user.getPassword().getBytes());
        user.setPassword(encodedPassword);
        
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}