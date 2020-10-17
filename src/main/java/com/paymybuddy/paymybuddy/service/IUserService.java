package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.dto.UserRegistrationDTO;
import com.paymybuddy.paymybuddy.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {

    UserRegistrationDTO registerUser(UserRegistrationDTO userDTO);

    void login(String userName, String password);

    User getUserByEmail(String email);

    void addConnection(String myEmail, String buddyEmail);

    User getConnection(String myEmail, String buddyEmail);
}
