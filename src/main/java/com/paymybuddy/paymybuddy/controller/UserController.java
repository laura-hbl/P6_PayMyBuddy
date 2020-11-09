package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.dto.ConnectionDTO;
import com.paymybuddy.paymybuddy.dto.ContactsDTO;
import com.paymybuddy.paymybuddy.dto.UserDTO;
import com.paymybuddy.paymybuddy.security.MyUserDetailsService;
import com.paymybuddy.paymybuddy.service.IUserService;
import com.paymybuddy.paymybuddy.util.LoginEmailRetriever;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Creates REST endpoints for operations on user data.
 *
 * @author Laura Habdul
 * @see IUserService
 * @see LoginEmailRetriever
 * @see MyUserDetailsService
 */
@RestController
public class UserController {

    /**
     * UserController logger.
     */
    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    /**
     * IUserService's implement class reference.
     */
    private final IUserService userService;

    /**
     * LoginEmailRetriever instance.
     */
    private final LoginEmailRetriever loginEmailRetriever;

    /**
     * Constructor of class UserController.
     * Initialize userService and loginEmailRetriever.
     *
     * @param userService          IUserService's implement class reference
     * @param loginEmailRetriever  LoginEmailRetriever instance
     */
    @Autowired
    public UserController(final IUserService userService, final LoginEmailRetriever loginEmailRetriever) {
        this.userService = userService;
        this.loginEmailRetriever = loginEmailRetriever;
    }

    /**
     * Registers a new user.
     *
     * @param user the user to be registered
     * @return ResponseEntity<UserDTO> The response object and Http Status generated
     */
    @PostMapping("/registration")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody final UserDTO user) {
        LOGGER.debug("Registration request with username {}", user.getEmail());

        UserDTO userSaved = userService.registerUser(user);

        LOGGER.info("User registration request - SUCCESS");
        return new ResponseEntity<>(userSaved, HttpStatus.CREATED);
    }

    /**
     * Adds a new connection.
     *
     * @param connectionDTO the buddy email to be added
     * @return ResponseEntity<ContactsDTO> The response object and Http Status generated
     */
    @PostMapping("/contact")
    public ResponseEntity<ContactsDTO> addConnection(@Valid @RequestBody final ConnectionDTO connectionDTO,
                                                     final HttpServletRequest request) {
        LOGGER.debug("Add connection request with buddy email {}", connectionDTO.getBuddyEmail());

        String ownerEmail = loginEmailRetriever.getUsername(request);

        ContactsDTO contacts = userService.addConnection(ownerEmail, connectionDTO.getBuddyEmail());

        LOGGER.info("Add connection request - SUCCESS");
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }
}
