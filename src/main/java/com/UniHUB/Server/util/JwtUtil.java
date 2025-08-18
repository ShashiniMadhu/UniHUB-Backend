package com.UniHUB.Server.util;

import java.util.Date;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final String secret = "secretsecretsecretsecretsecretsecretsecret123";
    private final long expirationMs = 86400000; // 1 day

    public String generateToken(String email, String role, Integer userId, Integer studentId, Integer lecturerId) {
        // Create the builder first
        JwtBuilder builder = Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()));

        // Add studentId / lecturerId if available
        if (studentId != null) {
            builder.claim("studentId", studentId);
        }
        if (lecturerId != null) {
            builder.claim("lecturerId", lecturerId);
        }

        // Build and return the token
        return builder.compact();
    }



    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secret.getBytes()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secret.getBytes()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String getRoleFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secret.getBytes()).build()
                .parseClaimsJws(token).getBody().get("role", String.class);
    }


}
