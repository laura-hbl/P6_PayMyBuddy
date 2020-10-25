package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.dto.UserDTO;
import com.paymybuddy.paymybuddy.model.User;

public interface IUserService {

    UserDTO registerUser(UserDTO userDTO);

    User getUserByEmail(String email);

    void addConnection(String username, String buddyEmail);
}

