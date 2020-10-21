package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.dto.UserDTO;
import com.paymybuddy.paymybuddy.model.User;

public interface IUserService {

    UserDTO registerUser(UserDTO userDTO);

    void login(String userName, String password);

    User getUserByEmail(String email);

    void addConnection(String ownerEmail, String buddyEmail);
}

