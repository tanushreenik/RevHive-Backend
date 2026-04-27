package com.project.revhive.demo.service;

import com.project.revhive.demo.dto.request.RegisterRequest;
import com.project.revhive.demo.enums.Role;
import com.project.revhive.demo.model.User;
import com.project.revhive.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

  public User register(RegisterRequest registerRequest)
  {
      if(userRepository.findByEmail(registerRequest.getEmail()).isPresent())
      {
          throw new RuntimeException("Email already exists");
      }
      if(userRepository.findByUsername(registerRequest.getUsername()).isPresent())
      {
          throw new RuntimeException("User name is already present");
      }

      User user=User.builder().username(registerRequest.getUsername())
              .email(registerRequest.getEmail())
              .password(passwordEncoder.encode(registerRequest.getPassword()))
              .role(Role.USER)
              .bio(registerRequest.getBio())
//              .avatarUrl(registerRequest.getAvatarUrl())
              .build();

      return userRepository.save(user);

  }

}
