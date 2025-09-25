package com.example.billiardsstore.repository;

import com.example.billiardsstore.model.Role;
import com.example.billiardsstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Long countByRole(Role role);
}
