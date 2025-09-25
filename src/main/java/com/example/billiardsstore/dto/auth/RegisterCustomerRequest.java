package com.example.billiardsstore.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterCustomerRequest(
        String username,
        String password,
        String fullName,
        String email,
        String phoneNumber,
        String address
) {}

