package com.shreyansh.User_Service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class homeController {
    @GetMapping("/")
    public String greet(){
        return "Hello to User Service";
    }
}
