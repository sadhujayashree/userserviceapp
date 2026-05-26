package dev.jaya.userservice.services;

import dev.jaya.userservice.configurations.JwtConfig;
import dev.jaya.userservice.models.Role;
import dev.jaya.userservice.models.User;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public JwtService(JwtConfig jwtConfig, @Qualifier("jwtSecretKey") SecretKey secretKey) {
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    public String generateAccessToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtConfig.getExpiration());

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("roles", roleNames(user))
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtConfig.getRefreshExpiration());

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("roles", roleNames(user))
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    public String extractUser(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private List<String> roleNames(User user) {
        if (user.getRoles() == null) {
            return Collections.emptyList();
        }
        return user.getRoles().stream()
                .map(Role::getName)
                .toList();
    }
}
