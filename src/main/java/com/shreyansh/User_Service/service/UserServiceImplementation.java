package com.shreyansh.User_Service.service;

import com.shreyansh.User_Service.config.JWTProvider;
import com.shreyansh.User_Service.modal.User;
import com.shreyansh.User_Service.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImplementation {
    @Autowired
    private UserRepo userRepo;

    public User getUserProfile(String jwt){
        String email= JWTProvider.getEmailFromJwtToken(jwt);
        return userRepo.findByEmail(email);
    }
    public List<User> getAllUsers(){
        return userRepo.findAll();
    }
}
