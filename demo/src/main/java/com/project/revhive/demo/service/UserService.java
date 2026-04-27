package com.project.revhive.demo.service;

import com.project.revhive.demo.dto.request.RegisterRequest;
import com.project.revhive.demo.enums.Role;
import com.project.revhive.demo.model.User;
import com.project.revhive.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger logger= (Logger) LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

  public User register(RegisterRequest registerRequest)
  {

      logger.info("Register request received for email: {} ");
      if(userRepository.findByEmail(registerRequest.getEmail()).isPresent())
      {
          logger.warning("Email already exists");
          throw new RuntimeException("Email already exists");
      }
      if(userRepository.findByUsername(registerRequest.getUsername()).isPresent())
      {
          logger.warning("Username already exists");
          throw new RuntimeException("User name is already present");
      }

      User user=User.builder().username(registerRequest.getUsername())
              .email(registerRequest.getEmail())
              .password(passwordEncoder.encode(registerRequest.getPassword()))
              .role(Role.USER)
              .bio(registerRequest.getBio())
//              .avatarUrl(registerRequest.getAvatarUrl())
              .build();

      logger.info("User successfully registered");

      return userRepository.save(user);

  }

}
