package com.project.revhive.demo.controller;


import com.project.revhive.demo.dto.request.LoginRequest;
import com.project.revhive.demo.dto.request.RegisterRequest;
import com.project.revhive.demo.dto.response.LoginResponse;
import com.project.revhive.demo.model.User;
import com.project.revhive.demo.repository.UserRepository;
import com.project.revhive.demo.service.UserService;
import com.project.revhive.demo.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Tag(name = "AUTH API" , description = "User Authentication options")
public class UserController {
    private  static final Logger logger=  LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserRepository userRepository;
    private final FollowService followService;

    @Operation(summary = "End point to register a new user")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request)
    {
        logger.info("Incoming register request with email: {}", request.getEmail());
        User user= userService.register(request);
        logger.info("User  registration successfully {}", request.getEmail());
        return ResponseEntity.ok(user);
    }


     @Operation(summary = "User login endpoint and generate JWT Token")
     @PostMapping("/login")
     public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest)
     {
         try {
             logger.info("Login request for: {}", loginRequest.getUserNameOrEmail());
             LoginResponse response = userService.login(loginRequest);
             logger.info("Login Successful");
             return ResponseEntity.ok(response);
         } catch (RuntimeException e) {
             logger.error("Login failed: {}", e.getMessage());

             String message = e.getMessage() == null ? "Login failed" : e.getMessage();
             if (message.contains("User not found") || message.contains("Invalid password")) {
                 return ResponseEntity.status(401).body(new HashMap<String, String>() {{
                     put("status", "401");
                     put("message", message);
                     put("error", "Unauthorized");
                 }});
             }

             return ResponseEntity.status(500).body(new HashMap<String, String>() {{
                 put("status", "500");
                 put("message", message);
                 put("error", "Server Error");
             }});
         }
     }

     @Operation(summary = "Get user profile with stats (posts, followers, following)")
     @GetMapping("/profile/{userId}")
     public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
         try {
             User user = userRepository.findById(userId)
                     .orElseThrow(() -> new RuntimeException("User not found"));

             long followersCount = followService.getFollowersCount(userId);
             long followingCount = followService.getFollowingCount(userId);

             Map<String, Object> response = new HashMap<>();
             response.put("id", user.getId());
             response.put("username", user.getUsername());
             response.put("email", user.getEmail());
             response.put("bio", user.getBio());
             response.put("avatarUrl", user.getAvatarUrl());
             response.put("followersCount", followersCount);
             response.put("followingCount", followingCount);
             response.put("postsCount", 0); // Will be updated when posts are stored
             response.put("role", user.getRole().name());
             response.put("isActive", user.isActive());
             response.put("createdAt", user.getCreatedAt());

             return ResponseEntity.ok(response);
         } catch (Exception e) {
             logger.error("Error fetching user profile: {}", e.getMessage());
             return ResponseEntity.status(404).body(new HashMap<String, String>() {
                 {
                     put("error", "User not found");
                     put("message", e.getMessage());
                 }
             });
         }
     }

}
