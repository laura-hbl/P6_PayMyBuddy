package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.dto.UserDTO;
import com.paymybuddy.paymybuddy.exception.DataAlreadyRegisteredException;
import com.paymybuddy.paymybuddy.model.BuddyAccount;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import com.paymybuddy.paymybuddy.security.MyUserDetailsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService implements IUserService {

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final MyUserDetailsService myUserDetailsService;

    private final IBuddyAccountService buddyAccountService;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(final UserRepository userRepository, final IBuddyAccountService buddyAccountService,
                       final BCryptPasswordEncoder passwordEncoder, final MyUserDetailsService myUserDetailsService) {
        this.userRepository = userRepository;
        this.buddyAccountService = buddyAccountService;
        this.passwordEncoder = passwordEncoder;
        this.myUserDetailsService = myUserDetailsService;
    }

    public UserDTO registerUser(UserDTO userDTO) {
        LOGGER.debug("Inside UserService.registerUser for username : " + userDTO.getEmail());
        User userFound = userRepository.findByEmail(userDTO.getEmail());

        if (userFound != null) {
            throw new DataAlreadyRegisteredException("An User is already registered with this email");
        }
        User user = userRepository.save(new User(userDTO.getFirstName(), userDTO.getLastName(),
                userDTO.getEmail(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getPhone()));

        buddyAccountService.saveBuddyAccount(new BuddyAccount(user, BigDecimal.ZERO));

        return new UserDTO(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(),
                user.getPhone());
    }

    public void login(String username, String password) {
        LOGGER.debug("Inside UserService.login for username : " + username);
        UserDetails user = myUserDetailsService.loadUserByUsername(username);

        boolean passwordsMatch = passwordEncoder.matches(password, user.getPassword());

        if (!passwordsMatch) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public User getUserByEmail(String email) {
        LOGGER.debug("Inside UserService.getUserByEmail for email : " + email);
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("No user registered with this email");
        }

        return user;
    }

    public void addConnection(String ownerEmail, String buddyEmail) {
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
        userRepository.save(owner);
    }
}
