package com.shreyansh.User_Service.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class homeController {
    @GetMapping("/")
    public String greet() {
        return "Welcome to User Service";
    }

    @GetMapping("/csrf-token")
    public ResponseEntity<Map<String, String>> getCsrfToken(HttpServletRequest request) {
        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        Map<String, String> response = new HashMap<>();
        
        if (csrf != null) {
            response.put("token", csrf.getToken());
            response.put("headerName", csrf.getHeaderName());
            response.put("parameterName", csrf.getParameterName());
            
            // For debugging: log the token
            System.out.println("Generated CSRF Token: " + csrf.getToken());
            System.out.println("Expected Header Name: " + csrf.getHeaderName());
            
            // Check if cookie is already set
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("XSRF-TOKEN".equals(cookie.getName())) {
                        System.out.println("Existing XSRF-TOKEN cookie: " + cookie.getValue());
                    }
                }
            }
        } else {
            response.put("error", "CSRF token not available");
            System.out.println("CSRF token not available in request");
        }
        
        return ResponseEntity.ok(response);
    }

}
