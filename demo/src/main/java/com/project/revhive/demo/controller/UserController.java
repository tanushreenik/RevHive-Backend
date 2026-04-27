package com.project.revhive.demo.controller;


import com.project.revhive.demo.dto.request.RegisterRequest;
import com.project.revhive.demo.model.User;
import com.project.revhive.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Tag(name = "AUTH API" , description = "User Authentication options")
public class UserController {
    private  static final Logger logger= (Logger) LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Operation(summary = "End point to register a new user")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request)
    {
        logger.info("Incoming register request with email: "+ request.getEmail());
        User user= userService.register(request);
        logger.info("User  registration successfully "+ request.getEmail());
        return ResponseEntity.ok(user);
    }
}
