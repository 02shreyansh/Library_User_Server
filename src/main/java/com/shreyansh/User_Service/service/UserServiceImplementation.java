package com.shreyansh.User_Service.service;

import com.shreyansh.User_Service.dto.UserDTO;
import com.shreyansh.User_Service.modal.User;
import com.shreyansh.User_Service.repository.UserRepo;
import com.shreyansh.User_Service.utils.Security.JWTProvider;

import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImplementation {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTProvider jwtProvider;

    public UserDTO getUserProfile(String jwt) {
        Claims claims = jwtProvider.extractAllClaims(jwt);
        String email = claims.get("email", String.class);
        User user = userRepo.findByEmail(email);
        return new UserDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepo.findAll();
        return users.stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public void deleteUser() {
        userRepo.deleteAll();
    }
}
