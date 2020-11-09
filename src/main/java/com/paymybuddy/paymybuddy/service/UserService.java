package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.dto.ContactsDTO;
import com.paymybuddy.paymybuddy.dto.UserDTO;
import com.paymybuddy.paymybuddy.exception.DataAlreadyRegisteredException;
import com.paymybuddy.paymybuddy.model.BuddyAccount;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Contains methods that allow interaction between User business logic and User repository.
 *
 * @author Laura Habdul
 * @see IBuddyAccountService
 * @see IUserService
 */
@Service
public class UserService implements IUserService {

    /**
     * UserService logger.
     */
    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    /**
     * UserRepository instance.
     */
    private final UserRepository userRepository;

    /**
     * IBuddyAccountService's implement class reference.
     */
    private final IBuddyAccountService buddyAccountService;

    /**
     * BCryptPasswordEncoder instance.
     */
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructor of class UserService.
     * Initialize userRepository, buddyAccountService and passwordEncoder.
     *
     * @param userRepository      UserRepository instance
     * @param buddyAccountService IBuddyAccountService's implement class reference
     * @param passwordEncoder     BCryptPasswordEncoder instance
     */
    @Autowired
    public UserService(final UserRepository userRepository, final IBuddyAccountService buddyAccountService,
                       final BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.buddyAccountService = buddyAccountService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Calls UserRepository's findByEmail method to retrieves the user with the user email and checks if user is not
     * already registered, if so DataAlreadyRegisteredException is thrown. Else, encrypts the user password by calling
     * passwordEncoder's encode method, converts the given userDTO object to model object to save it by calling
     * UserRepository's save method and then creates the user's buddy account by calling buddyAccountService's
     * saveBuddyAccount method.
     *
     * @param userDTO the user to be registered
     * @return The user saved converted to a UserDTO object
     */
    public UserDTO registerUser(final UserDTO userDTO) {
        LOGGER.debug("Inside UserService.registerUser for username : " + userDTO.getEmail());
        User userFound = userRepository.findByEmail(userDTO.getEmail());

        if (userFound != null) {
            throw new DataAlreadyRegisteredException("Registration failed. The email provided may be registered " +
                    "already");
        }
        String password = passwordEncoder.encode(userDTO.getPassword());
        User userToSave = new User(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
                password, userDTO.getPhone());

        User user = userRepository.save(userToSave);
        buddyAccountService.saveBuddyAccount(new BuddyAccount(user, BigDecimal.ZERO));

        return new UserDTO(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(),
                user.getPhone());
    }

    /**
     * Calls UserRepository's findByEmail method to retrieves the user with the user email and checks if user is
     * registered, if not UsernameNotFoundException is thrown.
     *
     * @param email of the user to be found
     * @return The user found
     */
    public User getUserByEmail(final String email) {
        LOGGER.debug("Inside UserService.getUserByEmail for email : " + email);
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("No user registered with this email");
        }

        return user;
    }

    /**
     * Calls UserRepository's findByEmail method to retrieves owner and buddy with the given owner and buddy
     * emails, checks if buddy to add exists in database and is not already add in owner's contact, if not
     * UsernameNotFoundException and DataAlreadyRegisteredException is thrown respectively. Else, buddy is add in owner
     * contact and owner is saved by calling UserRepository's save method.
     *
     * @param ownerEmail the owner email
     * @param buddyEmail the buddy to add email
     * @return ContactsDTO object that contains the owner contact list
     */
    public ContactsDTO addConnection(final String ownerEmail, final String buddyEmail) {
        LOGGER.debug("Inside UserService.addConnection");
        User owner = userRepository.findByEmail(ownerEmail);
        User buddyToAdd = userRepository.findByEmail(buddyEmail);

        if (buddyToAdd == null) {
            throw new UsernameNotFoundException("No buddy registered with this email");
        }
        if (owner.getContacts().contains(buddyToAdd)) {
            throw new DataAlreadyRegisteredException("This connection is already in your contacts");
        }
        owner.getContacts().add(buddyToAdd);
        User userSaved = userRepository.save(owner);

        ContactsDTO contactList = new ContactsDTO();
        for (User user : userSaved.getContacts()) {
            contactList.getContacts().add(user.getEmail());
        }

        return contactList;
    }
}
