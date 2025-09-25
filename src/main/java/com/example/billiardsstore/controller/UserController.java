package com.example.billiardsstore.controller;

import com.example.billiardsstore.dto.user.UserDTO;
import com.example.billiardsstore.dto.user.UserUpdateRequest;
import com.example.billiardsstore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public UserDTO getMe(Authentication auth) {
        return userService.getCurrentUser(auth.getName());
    }

    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public UserDTO updateMe(@RequestBody @Valid UserUpdateRequest req, Authentication auth) {
        return userService.updateUser(auth.getName(), req);
    }
}
