package com.shreyansh.User_Service.utils.helper;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final helper authUtils;

    public TokenResponse generateTokens(PayloadData payload) {
        try {
            String accessToken = authUtils.generateAccessToken(payload);
            String refreshToken = authUtils.generateRefreshToken(payload);
            
            return new TokenResponse(accessToken, refreshToken, payload);
        } catch (Exception e) {
            log.error("Error generating tokens", e);
            throw new RuntimeException("Failed to generate authentication tokens", e);
        }
    }
    
    public void setTokenCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        ResponseCookie accessTokenCookie = authUtils.createAccessTokenCookie(accessToken);
        ResponseCookie refreshTokenCookie = authUtils.createRefreshTokenCookie(refreshToken);
        
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }
    
    public String refreshAccessToken(String refreshToken) {
        Claims claims = authUtils.verifyRefreshToken(refreshToken);
        
        if (claims == null) {
            return null;
        }
        
        Date expiresAt = new Date((Long) claims.get("expiresAt"));
        if (expiresAt.before(new Date())) {
            return null;
        }
        
        PayloadData payload = new PayloadData(
            ((Integer) claims.get("id")).longValue(),
            claims.get("email").toString()
        );
        
        return authUtils.generateAccessToken(payload);
    }
    
    
    public boolean validatePassword(String enteredPassword, String savedPassword) {
        return authUtils.validatePassword(enteredPassword, savedPassword);
    }
    
    public String hashPassword(String password) {
        String salt = authUtils.generateSalt();
        return authUtils.generatePassword(password, salt);
    }
}
