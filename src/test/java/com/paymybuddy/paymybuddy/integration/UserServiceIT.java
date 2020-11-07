package com.paymybuddy.paymybuddy.integration;

import com.paymybuddy.paymybuddy.dto.ContactsDTO;
import com.paymybuddy.paymybuddy.dto.UserDTO;
import com.paymybuddy.paymybuddy.exception.DataAlreadyRegisteredException;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource({"/application-test.properties"})
@Sql(scripts = "/schema-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserServiceIT {

    @Autowired
    private UserService userService;

    @Test
    @Tag("RegisterUser")
    @DisplayName("If user is not registered, when registerUser, then return user saved")
    public void givenAnUnRegisteredUser_whenRegisterUser_thenUserSavedShouldBeReturned() {
        UserDTO user = new UserDTO("New", "New", "new@gmail.com", "newPassword",
                "111-111-111");

        UserDTO userSaved = userService.registerUser(user);

        assertThat(userSaved.getLastName()).isEqualTo("New");
        assertThat(userSaved.getPhone()).isEqualTo("111-111-111");
    }

    @Test(expected = DataAlreadyRegisteredException.class)
    @Tag("RegisterUser")
    @DisplayName("If user is already registered, when registerUser, then throw DataAlreadyRegisteredException")
    public void givenARegisteredUser_whenRegisterUser_thenDataAlreadyRegisteredExceptionIsThrown() {
        UserDTO user = new UserDTO("Brad", "Pitt", "brad@gmail.com",
                "$2a$10$Ua2VaNYHIQvHTgnO5K5hbOFP", "111-111-111");

        userService.registerUser(user);
    }

    @Test
    @Tag("GetUserByEmail")
    @DisplayName("If given email is registered, when getUserByEmail, then return corresponding user")
    public void givenARegisteredEmail_whenGetUserByEmail_thenCorrespondingUserShouldBeReturned() {
        User user = userService.getUserByEmail("brad@gmail.com");

        assertThat(user.getLastName()).isEqualTo("Pitt");
        assertThat(user.getPhone()).isEqualTo("111-111-111");
    }

    @Test(expected = UsernameNotFoundException.class)
    @Tag("GetUserByEmail")
    @DisplayName("If given email is not registered, when getUserByEmail, then throw UsernameNotFoundException")
    public void givenAnRegisteredEmail_whenGetUserByEmail_thenUsernameNotFoundExceptionIsThrown() {
        userService.getUserByEmail("unregistered@gmail.com");
    }

    @Test
    @Tag("addConnection")
    @DisplayName("Given a buddy email to add, when addConnection, then buddy should be add in contact")
    public void givenABuddyEmailToAdd_whenAddConnection_thenBuddyShouldBeAddInContact() {
        ContactsDTO contacts = userService.addConnection("brad@gmail.com","leonardo@gmail.com");

        assertThat(contacts.getContacts()).contains("leonardo@gmail.com");
    }

    @Test(expected = UsernameNotFoundException.class)
    @Tag("addConnection")
    @DisplayName("If buddy to add is not found, when addConnection, then throw UsernameNotFoundException")
    public void givenAnUnFoundBuddy_whenAddConnection_thenUsernameNotFoundExceptionIsThrown() {
        userService.addConnection("brad@gmail.com", "unfoundbuddy@gmail.com");
    }

    @Test(expected = DataIntegrityViolationException.class)
    @Tag("addConnection")
    @DisplayName("If buddy is already in contact, when addConnection, then throw DataIntegrityViolationException")
    public void givenAnAlreadyAddedBuddy_whenAddConnection_thenDataIntegrityViolationExceptionIsThrown() {
        userService.addConnection("brad@gmail.com", "tom@gmail.com");
    }
}
