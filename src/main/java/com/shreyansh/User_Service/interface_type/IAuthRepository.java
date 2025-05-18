package com.shreyansh.User_Service.interface_type;

import com.shreyansh.User_Service.db.User;
import com.shreyansh.User_Service.model.RequestHandler;

import java.util.Optional;

public interface IAuthRepository {
    User signup(RequestHandler.SignupDto data);
    Optional<User> findCustomerByEmail(String email);
    void updateRefreshToken(Long userId, String refreshToken);
}
