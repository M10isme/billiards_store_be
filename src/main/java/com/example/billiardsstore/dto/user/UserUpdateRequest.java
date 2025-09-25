package com.example.billiardsstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @NotBlank(message = "Full name is required") @Size(max = 100) String fullName,
        @Email(message = "Invalid email") @Size(max = 100) String email,
        @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Invalid phone number") String phoneNumber,
        @Size(max = 255) String address
) {}
