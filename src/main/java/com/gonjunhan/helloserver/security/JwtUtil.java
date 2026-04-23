package com.gonjunhan.helloserver.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration-millis}")
    private long expirationMillis;

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date expire = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expire)
                .signWith(getSignKey())
                .compact();
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}