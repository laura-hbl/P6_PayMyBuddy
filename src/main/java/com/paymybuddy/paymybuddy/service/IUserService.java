package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.dto.ContactsDTO;
import com.paymybuddy.paymybuddy.dto.UserDTO;
import com.paymybuddy.paymybuddy.model.User;

/**
 * UserService interface.
 *
 * @author Laura Habdul
 */
public interface IUserService {

    /**
     * Registers a new User in database.
     *
     * @param userDTO the user to be registered
     * @return The user saved converted to a UserDTO object
     */
    UserDTO registerUser(final UserDTO userDTO);

    /**
     * Retrieves an User based on the given email.
     *
     * @param email of the user to be found
     * @return The user found
     */
    User getUserByEmail(final String email);

    /**
     * Adds a buddy to contacts.
     *
     * @param ownerEmail the owner email
     * @param buddyEmail the buddy to add email
     * @return ContactsDTO object that contains the owner contact list
     */
    ContactsDTO addConnection(final String ownerEmail, final String buddyEmail);
}
