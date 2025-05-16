package com.shreyansh.User_Service.controller;

import com.shreyansh.User_Service.dto.UserDTO;
import com.shreyansh.User_Service.service.UserServiceImplementation;
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
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class userController {
    @Autowired
    private UserServiceImplementation userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfile(@CookieValue("token") String jwt) {

        UserDTO userDTO = userService.getUserProfile(jwt);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<UserDTO>> getUsers() {

        List<UserDTO> userDTOs = userService.getAllUsers();
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrfToken==null) {
            throw new Error("CSRF TOKEN absent");
        }
        userService.deleteUser();
        return new ResponseEntity<>("User Deleted", HttpStatus.OK);
    }
}
