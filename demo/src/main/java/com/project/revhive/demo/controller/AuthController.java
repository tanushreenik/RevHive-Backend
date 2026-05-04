package com.project.revhive.demo.controller;

import com.project.revhive.demo.model.User;
import com.project.revhive.demo.repository.UserRepository;
import com.project.revhive.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final UserRepository userRepository;
    @Autowired
    private AuthService authService;


    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(user);
    }



        @PostMapping("/forgot-password")
        public ResponseEntity<String> forgot(@RequestParam String email) {
            authService.forgotPassword(email);
            return ResponseEntity.ok("Reset link sent to email");
        }

        @PostMapping("/reset-password")
        public ResponseEntity<String> reset(
                @RequestParam String token,
                @RequestParam String newPassword) {

            authService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Password updated");
        }
    }
