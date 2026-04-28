package com.project.revhive.demo.service;

import com.project.revhive.demo.dto.request.RegisterRequest;
import com.project.revhive.demo.enums.Role;
import com.project.revhive.demo.model.User;
import com.project.revhive.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())  // Plain text password
                .role(Role.USER)
                .isActive(true)
                .followersCount(0)
                .followingCount(0)
                .build();

        return userRepository.save(user);
    }
}