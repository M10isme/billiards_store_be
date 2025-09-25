package com.example.billiardsstore.controller;

import com.example.billiardsstore.dto.auth.AuthResponse;
import com.example.billiardsstore.dto.auth.LoginRequest;
import com.example.billiardsstore.dto.auth.RegisterCustomerRequest;
import com.example.billiardsstore.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterCustomerRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}

