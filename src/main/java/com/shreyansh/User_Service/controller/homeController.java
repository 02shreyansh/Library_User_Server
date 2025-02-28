package com.shreyansh.User_Service.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class homeController {
    @GetMapping("/")
    public String greet() {
        return "Welcome to User Service";
    }

    @GetMapping("/csrf-token")
    public CsrfToken csrfToken(CsrfToken token) {
        return token;
    }
}
