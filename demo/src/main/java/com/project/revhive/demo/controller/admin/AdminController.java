package com.project.revhive.demo.controller;

import com.project.revhive.demo.enums.Role;
import com.project.revhive.demo.enums.Status;
import com.project.revhive.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;

    @GetMapping("/stats")
    public Map<String, Long> getStats() {

        Map<String, Long> stats = new HashMap<>();

        stats.put("totalUsers", userRepository.count());

        stats.put(
                "activeUsers",
                userRepository.countByStatus("ACTIVE")
        );

        stats.put(
                "blockedUsers",
                userRepository.countByStatus("BLOCKED")
        );

        stats.put(
                "premiumUsers",
                userRepository.countByRole(Role.PREMIUM)
        );

        stats.put(
                "admins",
                userRepository.countByRole(Role.ADMIN)
        );

        return stats;
    }
}
