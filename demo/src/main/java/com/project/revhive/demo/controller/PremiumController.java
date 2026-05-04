package com.project.revhive.demo.controller;


import com.project.revhive.demo.enums.Role;
import com.project.revhive.demo.model.User;
import com.project.revhive.demo.repository.UserRepository;
import com.project.revhive.demo.security.JWTUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/premium")
public class PremiumController {

    private final UserRepository repo;
    private final JWTUtil jwtUtil;

    public PremiumController(UserRepository repo, JWTUtil jwtUtil) {
        this.repo = repo;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/upgrade")
    public String upgrade(Authentication auth) {
        User user = repo.findByEmail(auth.getName()).orElseThrow();


        user.setRole(Role.PREMIUM);

        user.setPremium(true);
        user.setPremiumExpiry(LocalDate.now().plusMonths(1));

        repo.save(user);


        return jwtUtil.generateToken(user);
    }

    @PreAuthorize("isAuthenticated()")
//    @PreAuthorize("hasRole('PREMIUM')")
    @GetMapping("/feature")
    public String premiumFeature() {
        return "Premium Feature Accessed";
    }
}
