package com.shreyansh.User_Service.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
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
        } else {
            response.put("error", "CSRF token not available");
        }
        return ResponseEntity.ok(response);
    }

}
