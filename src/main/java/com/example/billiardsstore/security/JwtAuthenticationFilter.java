package com.example.billiardsstore.security;

import com.example.billiardsstore.model.User;
import com.example.billiardsstore.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println("JWT Filter - Request: " + request.getRequestURI());
        System.out.println("JWT Filter - Authorization header: " + auth);
        
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            System.out.println("JWT Filter - Token: " + token.substring(0, Math.min(20, token.length())) + "...");
            
            if (jwtService.isTokenValid(token)) {
                String username = jwtService.extractUsername(token);
                System.out.println("JWT Filter - Username extracted: " + username);
                
                Optional<User> userOpt = userRepository.findByUsername(username);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    System.out.println("JWT Filter - User found: " + user.getUsername() + ", Role: " + user.getRole());
                    
                    var authToken = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("JWT Filter - Authentication set with role: ROLE_" + user.getRole().name());
                } else {
                    System.out.println("JWT Filter - User not found for username: " + username);
                }
            } else {
                System.out.println("JWT Filter - Invalid token");
            }
        } else {
            System.out.println("JWT Filter - No Bearer token found");
        }
        filterChain.doFilter(request, response);
    }
}
