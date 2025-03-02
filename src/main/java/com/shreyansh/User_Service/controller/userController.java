package com.shreyansh.User_Service.controller;

import com.shreyansh.User_Service.modal.User;
import com.shreyansh.User_Service.service.UserServiceImplementation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class userController {
    @Autowired
    private UserServiceImplementation userService;
    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(@CookieValue("token") String jwt){

        User user=userService.getUserProfile(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<List<User>> getUsers(){

        List<User> users=userService.getAllUsers();
        return new ResponseEntity<>(users,HttpStatus.OK);
    }
    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(HttpServletRequest request){
        
        // 1. Check CSRF token from request attribute
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrfToken != null) {
            System.out.println("Expected CSRF Token: " + csrfToken.getToken());
            System.out.println("Expected Header Name: " + csrfToken.getHeaderName());
            System.out.println("Actual Header Value: " + request.getHeader(csrfToken.getHeaderName()));
        } else {
            System.out.println("No CSRF token found in request attributes!");
        }
        
        // 2. Print all headers
        System.out.println("--- All Request Headers ---");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println(headerName + ": " + request.getHeader(headerName));
        }
        
        // 3. Print all cookies
        System.out.println("--- All Request Cookies ---");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName() + ": " + cookie.getValue());
            }
        } else {
            System.out.println("No cookies found in request");
        }
        
        // Actual delete logic
        userService.deleteUser();
        return new ResponseEntity<>("User Deleted", HttpStatus.OK);
    }
}
