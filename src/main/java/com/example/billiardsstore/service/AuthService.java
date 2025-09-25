package com.example.billiardsstore.service;

import com.example.billiardsstore.dto.auth.AuthResponse;
import com.example.billiardsstore.dto.auth.LoginRequest;
import com.example.billiardsstore.dto.auth.RegisterCustomerRequest;
import com.example.billiardsstore.model.Role;
import com.example.billiardsstore.model.User;
import com.example.billiardsstore.repository.UserRepository;
import com.example.billiardsstore.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String register(RegisterCustomerRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("❌ Username already exists");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("❌ Email already exists");
        }

        User user = User.builder()
                .username(request.username())
                .passwordHash(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .address(request.address())
                .role(Role.CUSTOMER)
                .active(true)
                .build();

        userRepository.save(user);

        return "✅ Register success, please login";
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        return new AuthResponse(token, user.getUsername(), user.getRole());
    }
}

