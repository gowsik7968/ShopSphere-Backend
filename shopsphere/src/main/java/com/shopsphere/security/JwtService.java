package com.shopsphere.security;

import com.shopsphere.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final String SECRET_KEY =
            "ShopSphereSuperSecretKeyForJwtAuthentication2026";

    private final long EXPIRATION_TIME =
            1000 * 60 * 60 * 24;

    // Create signing key
    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    // Generate JWT token
    public String generateToken(User user) {

        return Jwts.builder()

                // Store user's email inside token
                .subject(user.getEmail())

                // Store user's name
                .claim("name", user.getName())

                // Store user's role
                .claim("role", user.getRole())

                // Token creation time
                .issuedAt(new Date())

                // Token expiration time
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + EXPIRATION_TIME
                        )
                )

                // Sign token
                .signWith(getSigningKey())

                // Create final JWT
                .compact();
    }

    // Extract email from JWT
    public String extractEmail(String token) {

        Claims claims = Jwts.parser()

                .verifyWith(getSigningKey())

                .build()

                .parseSignedClaims(token)

                .getPayload();

        return claims.getSubject();
    }

    // Check whether token is valid
    public boolean isTokenValid(
            String token,
            User user) {

        String email =
                extractEmail(token);

        return email.equals(user.getEmail())
                && !isTokenExpired(token);
    }

    // Check token expiration
    private boolean isTokenExpired(String token) {

        Claims claims = Jwts.parser()

                .verifyWith(getSigningKey())

                .build()

                .parseSignedClaims(token)

                .getPayload();

        return claims.getExpiration()
                .before(new Date());
    }
}