package com.courseflow.aiquiz.service;

import com.courseflow.aiquiz.dto.LoginRequest;
import com.courseflow.aiquiz.dto.RegisterRequest;
import com.courseflow.aiquiz.dto.UserResponse;
import com.courseflow.aiquiz.entity.User;
import com.courseflow.aiquiz.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User saved = userRepository.save(user);
        return new UserResponse(saved.getId(), saved.getFullName(), saved.getEmail());
    }

    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return new UserResponse(user.getId(), user.getFullName(), user.getEmail());
    }
}
