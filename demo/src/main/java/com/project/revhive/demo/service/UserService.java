package com.project.revhive.demo.service;

//import com.project.revhive.demo.dto.request.LoginRequest;
import com.project.revhive.demo.dto.request.LoginRequest;
import com.project.revhive.demo.dto.request.RegisterRequest;
import com.project.revhive.demo.dto.response.LoginResponse;
import com.project.revhive.demo.enums.Role;
import com.project.revhive.demo.model.User;
import com.project.revhive.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger logger=  LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

  public User register(RegisterRequest registerRequest)
  {

      logger.info("Register request received for email: {} ");
      if(userRepository.findByEmail(registerRequest.getEmail()).isPresent())
      {
          logger.warn("Email already exists");
          throw new RuntimeException("Email already exists");
      }
      if(userRepository.findByUsername(registerRequest.getUsername()).isPresent())
      {
          logger.warn("Username already exists");
          throw new RuntimeException("User name is already present");
      }

      User user=User.builder().username(registerRequest.getUsername())
              .email(registerRequest.getEmail())
              .password(passwordEncoder.encode(registerRequest.getPassword()))
              .role(Role.USER)
              .bio(registerRequest.getBio())
              .username(registerRequest.getUsername())
//              .avatarUrl(registerRequest.getAvatarUrl())
              .build();

      logger.info("User successfully registered");

      return userRepository.save(user);

  }


  public LoginResponse login(LoginRequest loginRequest)
  {
      User user=userRepository.findByEmailOrUsername(loginRequest.getUserNameOrEmail(),loginRequest.getUserNameOrEmail())
              .orElseThrow(()-> new RuntimeException("User not found"));

      if(!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword()))
      {
          throw new RuntimeException("Invalid password");
      }

      String token= jwtService.generateToken(user);
      logger.info("User logged in successfully: {}",user.getEmail());

      return LoginResponse.builder()
              .token(token)
              .username(user.getUsername())
              .email(user.getEmail())
              .role(user.getRole().name())
              .build();
  }

}
