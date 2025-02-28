package com.shreyansh.User_Service.controller;

import com.shreyansh.User_Service.modal.User;
import com.shreyansh.User_Service.service.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
