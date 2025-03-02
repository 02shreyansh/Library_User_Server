package com.shreyansh.User_Service.service;

import com.shreyansh.User_Service.config.JWTProvider;
import com.shreyansh.User_Service.modal.User;
import com.shreyansh.User_Service.repository.UserRepo;

import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImplementation {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTProvider jwtProvider;

    public User getUserProfile(String jwt){
        Claims claims = jwtProvider.extractAllClaims(jwt);
        String email = claims.get("email", String.class);
        return userRepo.findByEmail(email);
    }
    public List<User> getAllUsers(){
        return userRepo.findAll();
    }
    public void deleteUser(){
        userRepo.deleteAll();
    }
}
