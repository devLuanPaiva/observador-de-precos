package com.luanpaiva.observador_de_precos.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
    @Value("${spring.security.jwt.secret}")
    private String jwtSecret;

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    private String buildToken(UUID userId, String name, String email, String type, long expirationMillis) {
        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + expirationMillis);

        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("name", name)
                .claim("email", email)
                .claim("type", type)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UUID userId, String name, String email, String type) {
        long expiration = "refresh".equalsIgnoreCase(type)
                ? 1000L * 60 * 60 * 24 * 7 
                : 1000L * 60 * 60; 

        return buildToken(userId, name, email, type, expiration);
    }

    
    public String generateAccessToken(UUID userId, String name, String email) {
        return buildToken(userId, name, email, "access", 1000L * 60 * 60);
    }

    public String generateRefreshToken(UUID userId, String name, String email) {
        return buildToken(userId, name, email, "refresh", 1000L * 60 * 60 * 24 * 7);
    }

    public UUID extractUserId(String token) {
    Claims claims = Jwts.parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();

    return UUID.fromString(claims.getSubject());
}
}
