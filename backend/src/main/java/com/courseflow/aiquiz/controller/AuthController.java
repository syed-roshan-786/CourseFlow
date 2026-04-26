package com.courseflow.aiquiz.controller;

import com.courseflow.aiquiz.dto.LoginRequest;
import com.courseflow.aiquiz.dto.RegisterRequest;
import com.courseflow.aiquiz.dto.UserResponse;
import com.courseflow.aiquiz.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            if (request.getFullName() == null || request.getEmail() == null || request.getPassword() == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "fullName, email, password are required"));
            }

            UserResponse user = authService.register(request);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("user", user);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("already registered")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "Email already registered"));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Server error"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            if (request.getEmail() == null || request.getPassword() == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "email and password are required"));
            }

            UserResponse user = authService.login(request);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("user", user);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid credentials"));
        }
    }
}
