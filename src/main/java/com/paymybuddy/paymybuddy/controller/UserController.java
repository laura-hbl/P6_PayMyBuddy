package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.dto.ConnectionDTO;
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

@RestController
public class UserController {

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    private final IUserService userService;

    private final LoginEmailRetriever loginEmailRetriever;

    private final MyUserDetailsService myUserDetailsService;

    @Autowired
    public UserController(final IUserService userService, final LoginEmailRetriever loginEmailRetriever,
                          final MyUserDetailsService myUserDetailsService) {
        this.userService = userService;
        this.loginEmailRetriever = loginEmailRetriever;
        this.myUserDetailsService = myUserDetailsService;
    }

    @PostMapping("/registration")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO user) {
        LOGGER.debug("Registration request with username {}", user.getEmail());

        UserDTO userSaved = userService.registerUser(user);

        LOGGER.info("User registration request - SUCCESS");
        return new ResponseEntity<>(userSaved, HttpStatus.CREATED);
    }

    @PostMapping("/contact")
    public String addConnection(@Valid @RequestBody ConnectionDTO connectionDTO, HttpServletRequest request) {
        LOGGER.debug("Add connection request with buddy email {}", connectionDTO.getBuddyEmail());

        String ownerEmail = loginEmailRetriever.getUsername(request);

        userService.addConnection(ownerEmail, connectionDTO.getBuddyEmail());

        LOGGER.info("Add connection request - SUCCESS");
        return "Connection is add successfully!";
    }
}
