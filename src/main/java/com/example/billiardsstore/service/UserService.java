package com.example.billiardsstore.service;

import com.example.billiardsstore.dto.user.UserDTO;
import com.example.billiardsstore.model.User;
import com.example.billiardsstore.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.example.billiardsstore.dto.user.UserUpdateRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class UserService {
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
    public UserDTO getCurrentUser(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return new UserDTO(user.getFullName(), user.getEmail(), user.getPhoneNumber(), user.getAddress());
    }
    public UserDTO updateUser(String username, UserUpdateRequest req) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // prevent changing to an email that's already used by another account
        if (req.email() != null && !req.email().equals(user.getEmail()) && userRepo.existsByEmail(req.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
        }

        // apply updates (fullName is required by validation)
        user.setFullName(req.fullName());
        user.setEmail(req.email());
        user.setPhoneNumber(req.phoneNumber());
        user.setAddress(req.address());
        userRepo.save(user);
        return new UserDTO(user.getFullName(), user.getEmail(), user.getPhoneNumber(), user.getAddress());
    }
}
