package com.shreyansh.User_Service.controller;

import com.shreyansh.User_Service.db.User;
import com.shreyansh.User_Service.repository.UserRepo;
import com.shreyansh.User_Service.service.CustomerUserService;
import com.shreyansh.User_Service.utils.AuthResponse;
import com.shreyansh.User_Service.utils.LoginRequest;
import com.shreyansh.User_Service.utils.Security.JWTProvider;
import com.shreyansh.User_Service.utils.Security.RateLimitingService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerUserService customerUserService;
    @Autowired
    private CsrfTokenRepository csrfTokenRepository;
    @Autowired
    private RateLimitingService rateLimitingService;
    @Autowired
    private PasswordEncoder encoder;
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFirstName();
        
        User isEmailExist = userRepo.findByEmail(email);
        if (isEmailExist != null) {
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("Email already exists");
            authResponse.setStatus(false);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(authResponse);
        }
        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setFirstName(fullName);
        createdUser.setPassword(encoder.encode(password));
        userRepo.save(createdUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = JWTProvider.generateToken(authentication,fullName);
        ResponseCookie jwtCookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("none")
                .maxAge(Duration.ofHours(24))
                .path("/")
                .build();
        CsrfToken csrfToken = csrfTokenRepository.generateToken(request);
        csrfTokenRepository.saveToken(csrfToken, request, response);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Registered");
        authResponse.setStatus(true);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(authResponse);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest, HttpServletRequest request,
            HttpServletResponse response) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        String full_name = loginRequest.getFull_name();
        if (rateLimitingService.isAccountLocked(email)) {
            long remainingTime = rateLimitingService.getRemainingLockoutTime(email);
            AuthResponse authResponse = new AuthResponse();
            authResponse.setStatus(false);
            authResponse.setMessage("Account is temporarily locked due to multiple failed attempts. Try again in "
                    + remainingTime + " minutes.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse);
        }
        try {
            Authentication authentication = authenticate(email, password);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            rateLimitingService.resetFailedAttempts(email);
            String token = JWTProvider.generateToken(authentication,full_name);
            ResponseCookie jwtCookie = ResponseCookie.from("token", token)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("none")
                    .maxAge(Duration.ofHours(24))
                    .path("/")
                    .build();
            CsrfToken csrfToken = csrfTokenRepository.generateToken(request);
            csrfTokenRepository.saveToken(csrfToken, request, response);
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("Login Success");
            authResponse.setJwt(token);
            authResponse.setStatus(true);
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(authResponse);

        } catch (BadCredentialsException e) {
            int attempts = rateLimitingService.recordFailedLogin(email);
            int attemptsLeft = 5 - attempts; // Assuming 5 max attempts
            
            AuthResponse authResponse = new AuthResponse();
            authResponse.setStatus(false);
            
            if (attemptsLeft <= 0) {
                authResponse.setMessage("Account has been temporarily locked due to multiple failed attempts.");
            } else {
                authResponse.setMessage("Invalid credentials. " + attemptsLeft + " attempts remaining before account lockout.");
            }
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse);
        }

    }

    public Authentication authenticate(String email, String password) {
        UserDetails userDetails = customerUserService.loadUserByUsername(email);
        if (userDetails == null) {
            throw new BadCredentialsException("User not found");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
