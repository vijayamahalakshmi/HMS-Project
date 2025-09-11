package com.example.project.security;

import com.example.project.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.ttlMillis:3600000}") // default 1 hour
    private long ttlMillis;

    private SecretKey key;

    @PostConstruct
    void init() {
        // HS256 key must be >= 32 bytes
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generate a JWT for the given user.
     * Includes a unique JTI so each token is different even for the same user/time.
     */
    public String generateToken(User user) {
        Instant now = Instant.now();
        String jti = UUID.randomUUID().toString(); // unique per token

        return Jwts.builder()
                .id(jti)                                          // <-- unique token id
                .subject(user.getUsername())                      // or user.getEmail()
                .claim("uid", user.getId())
                .claim("role", user.getRole() != null ? user.getRole().getName() : "USER")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(ttlMillis)))
                .signWith(key, Jwts.SIG.HS256)                    // jjwt 0.12.x API
                .compact();
    }

    /** Return the JWT subject (we set it to username). */
    public String getSubject(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /** Optional helper: true if token is expired. */
    public boolean isExpired(String token) {
        Date exp = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return exp != null && exp.before(new Date());
    }
}
