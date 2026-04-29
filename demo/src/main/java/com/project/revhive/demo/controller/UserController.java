package com.project.revhive.demo.controller;


import com.project.revhive.demo.dto.request.LoginRequest;
import com.project.revhive.demo.dto.request.RegisterRequest;
import com.project.revhive.demo.dto.response.LoginResponse;
import com.project.revhive.demo.model.User;
import com.project.revhive.demo.repository.UserRepository;
import com.project.revhive.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Tag(name = "AUTH API" , description = "User Authentication options")
public class UserController {
    private  static final Logger logger=  LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserRepository userRepository;

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
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest)
    {
        logger.info("Login request for ",loginRequest.getUserNameOrEmail());
        LoginResponse response=userService.login(loginRequest);
        logger.info("Login Successfull ");
        return ResponseEntity.ok(response);
    }

}
