package com.project.revhive.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message="Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Bio is required")
    @Size(min=10,max=100, message = "Bio must be between 10 to 100 characters")
    private String bio;


//    @NotBlank(message = "Avatar URL is required")
//    private String avatarUrl;



}
