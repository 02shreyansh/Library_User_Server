package com.shreyansh.User_Service.utils.helper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.shreyansh.User_Service.config.Connect;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class helper {
    private final Connect connect;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public helper(Connect connect) {
        this.connect = connect;
        this.passwordEncoder = new BCryptPasswordEncoder(12);
    }

    public String generateSalt() {
        return passwordEncoder.encode("").substring(0, 29);
    }

    public String generatePassword(String password, String salt) {
        return passwordEncoder.encode(password);
    }

    public boolean validatePassword(String enteredPassword, String savedPassword) {
        return passwordEncoder.matches(enteredPassword, savedPassword);
    }

    public String generateRefreshToken(PayloadData payload) {
        try {
            String refreshTokenId = UUID.randomUUID().toString();
            String tokenFamily = UUID.randomUUID().toString();
            
            Date now = new Date();
            long expiryMillis = Long.parseLong(connect.getRefreshTokenExpiry());
            Date expiresAt = new Date(now.getTime() + expiryMillis);
            
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", payload.getId());
            claims.put("email", payload.getEmail());
            claims.put("tokenId", refreshTokenId);
            claims.put("tokenFamily", tokenFamily);
            claims.put("expiresAt", expiresAt.getTime());
            
            return Jwts.builder()
                    .claims(claims)
                    .issuedAt(now)
                    .expiration(expiresAt)
                    .signWith(connect.getRefreshPrivateKey(), Jwts.SIG.ES256) 
                    .compact();
        } catch (Exception e) {
            log.error("Error generating refresh token", e);
            throw new RuntimeException("Error generating refresh token", e);
        }
    }

    public String generateAccessToken(PayloadData payload) {
        try {
            Date now = new Date();
            // Parse the expiry time from the configuration
            long expiryMillis = Long.parseLong(connect.getAccessTokenExpiry());
            Date expiresAt = new Date(now.getTime() + expiryMillis);
            
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", payload.getId());
            claims.put("email", payload.getEmail());
            
            return Jwts.builder()
                    .claims(claims)
                    .issuedAt(now)
                    .expiration(expiresAt)
                    .signWith(connect.getAccessPrivateKey(), Jwts.SIG.ES256)
                    .compact();
        } catch (Exception e) {
            log.error("Error generating access token", e);
            throw new RuntimeException("Error generating access token", e);
        }
    }

    public Claims verifyRefreshToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(connect.getRefreshPublicKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.warn("Invalid refresh token", e);
            return null;
        }
    }

    public Claims verifyAccessToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(connect.getAccessPublicKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.warn("Invalid access token", e);
            return null;
        }
    }

    public ResponseCookie createAccessTokenCookie(String token) {
        boolean isProduction = "production".equals(System.getenv("SPRING_PROFILES_ACTIVE"));
        
        long expiryMillis = Long.parseLong(connect.getAccessTokenExpiry());
        Duration maxAge = Duration.ofMillis(expiryMillis);
        
        return ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(isProduction)
                .sameSite("Strict")
                .maxAge(maxAge)
                .path("/")
                .build();
    }

    public ResponseCookie createRefreshTokenCookie(String token) {
        boolean isProduction = "production".equals(System.getenv("SPRING_PROFILES_ACTIVE"));
        
        long expiryMillis = Long.parseLong(connect.getRefreshTokenExpiry());
        Duration maxAge = Duration.ofMillis(expiryMillis);
        
        return ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(isProduction)
                .sameSite("Strict")
                .maxAge(maxAge)
                .path("/")
                .build();
    }
}