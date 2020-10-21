package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.dto.UserDTO;
import com.paymybuddy.paymybuddy.exception.BadRequestException;
import com.paymybuddy.paymybuddy.service.IUserService;
import com.paymybuddy.paymybuddy.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final IUserService userService;

    private final Validator validator;

    @Autowired
    public UserController(final IUserService userService, final Validator validator) {
        this.userService = userService;
        this.validator = validator;
    }

    @PostMapping("/registration")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO user) {

        validator.validate(user);

        UserDTO userSaved = userService.registerUser(user);

        return new ResponseEntity<>(userSaved, HttpStatus.CREATED);
    }

    @PutMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestParam("username") final String username,
                                            @RequestParam("password") final String password) {

        if (username == null || username.trim().length() == 0 || password == null
                || password.trim().length() == 0) {
            throw new BadRequestException("Bad request : missing or incomplete information");
        }

        userService.login(username, password);

        return new ResponseEntity<>("Welcome to Pay my Buddy!", HttpStatus.OK);
    }

    @PostMapping("/contact")
    public ResponseEntity<Object> addConnection(@RequestParam("myEmail") final String myEmail,
                                                @RequestParam("buddyEmail") final String buddyEmail) {

        if (myEmail == null || myEmail.trim().length() == 0 || buddyEmail == null
                || buddyEmail.trim().length() == 0) {
            throw new BadRequestException("Bad request : missing or incomplete information");
        }

        userService.addConnection(myEmail, buddyEmail);

        return new ResponseEntity<>("Connection is add successfully!", HttpStatus.OK);
    }

    @DeleteMapping("/contact")
    public ResponseEntity<Object> deleteConnection(@RequestParam("myEmail") final String myEmail,
                                                   @RequestParam("buddyEmail") final String buddyEmail) {

        if (myEmail == null || myEmail.trim().length() == 0 || buddyEmail == null
                || buddyEmail.trim().length() == 0) {
            throw new BadRequestException("Bad request : missing or incomplete information");
        }

        userService.deleteConnection(myEmail, buddyEmail);

        return new ResponseEntity<>("Connection is delete successfully!", HttpStatus.OK);
    }
}
