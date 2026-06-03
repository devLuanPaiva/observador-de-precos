package com.luanpaiva.observador_de_precos.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.luanpaiva.observador_de_precos.modules.users.enums.UserRole;

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

    private String buildToken(UUID userId, String name, String email, String type, long expirationMillis,
            UserRole role) {
        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + expirationMillis);

        return Jwts.builder()
                .subject(userId.toString())
                .claim("name", name)
                .claim("email", email)
                .claim("type", type)
                .claim("role", role)
                .issuedAt(now)
                .expiration(exp)
                .signWith(getSignInKey())
                .compact();
    }

    public String generateToken(UUID userId, String name, String email, String type, UserRole role) {
        long expiration = "refresh".equalsIgnoreCase(type)
                ? 1000L * 60 * 60 * 24 * 7
                : 1000L * 60 * 60;

        return buildToken(userId, name, email, type, expiration, role);
    }

    public String generateAccessToken(UUID userId, String name, String email, UserRole role) {
        return buildToken(userId, name, email, "access", 1000L * 60 * 60, role);
    }

    public String generateRefreshToken(UUID userId, String name, String email, UserRole role) {
        return buildToken(userId, name, email, "refresh", 1000L * 60 * 60 * 24 * 7, role);
    }

    public UUID extractUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return UUID.fromString(claims.getSubject());
    }

    public Claims parseClaims(String refreshToken) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload();
    }
}
