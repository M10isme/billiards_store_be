package com.example.billiardsstore.dto.auth;

import com.example.billiardsstore.model.Role;

public record AuthResponse(String token, String tokenType, String username, Role role) {
    public AuthResponse(String token, String username, Role role) {
        this(token, "Bearer", username, role);
    }
}
