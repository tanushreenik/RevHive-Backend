package com.project.revhive.demo.controller.admin;

import com.project.revhive.demo.dto.response.AdminUserDTO;
import com.project.revhive.demo.enums.Role;
import com.project.revhive.demo.enums.Status;
import com.project.revhive.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserDTO>> getAllUsers() {

        List<AdminUserDTO> users = userRepository.findAll()
                .stream()
                .map(user -> new AdminUserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getAvatarUrl(),
                        user.isActive(),
                        user.isPremium(),
                        user.getFollowersCount(),
                        user.getFollowingCount(),
                        user.getRole().name()
                ))
                .toList();

        return ResponseEntity.ok(users);
    }
}
