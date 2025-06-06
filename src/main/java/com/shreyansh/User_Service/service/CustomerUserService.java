package com.shreyansh.User_Service.service;

import com.shreyansh.User_Service.db.User;
import com.shreyansh.User_Service.db.UserPrincipal;
import com.shreyansh.User_Service.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomerUserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepo.findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("User Not Found");
        }
        return new UserPrincipal(user);
    }
}
