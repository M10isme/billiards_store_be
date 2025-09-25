package com.example.billiardsstore.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final Key key;
    private final long expirationMs;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expiration}") long expiration) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expiration;
    }

    public String generateToken(String username, String role) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getBody().getSubject();
    }

    public String extractRole(String token) {
        Object r = getClaims(token).getBody().get("role");
        return r == null ? null : r.toString();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = getClaims(token).getBody();
            System.out.println("JWT Service - Token validation successful");
            System.out.println("JWT Service - Subject: " + claims.getSubject());
            System.out.println("JWT Service - Role: " + claims.get("role"));
            System.out.println("JWT Service - Expiration: " + claims.getExpiration());
            return true;
        } catch (JwtException ex) {
            System.out.println("JWT Service - Token validation failed: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    private Jws<Claims> getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }
}
