package com.paymybuddy.paymybuddy.unit.service;

import com.paymybuddy.paymybuddy.dto.ContactsDTO;
import com.paymybuddy.paymybuddy.dto.UserDTO;
import com.paymybuddy.paymybuddy.exception.DataAlreadyRegisteredException;
import com.paymybuddy.paymybuddy.model.BuddyAccount;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import com.paymybuddy.paymybuddy.service.BuddyAccountService;
import com.paymybuddy.paymybuddy.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private BuddyAccountService buddyAccountService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private static UserDTO userToSaveDTO;

    private static UserDTO userSavedDTO;

    private static User user;

    private static User buddyToAdd;

    @Before
    public void setUp() {
        userToSaveDTO = new UserDTO("Laura", "Habdul", "laurahbl@gmail.com",
                "myPassword", "0601331013");

        userSavedDTO = new UserDTO("Laura", "Habdul", "laurahbl@gmail.com",
                "HjuIY9jk5op&tc", "0601331013");

        user = new User("Laura", "Habdul", "laurahbl@gmail.com",
                "HjuIY9jk5op&tc", "0601331013");

        buddyToAdd = new User("Buddy", "Habdul", "buddy@gmail.com",
                "dr2@tde8èftKhY", "0607978866");
    }

    @Test
    @Tag("RegisterUser")
    @DisplayName("If user is not registered, when registerUser, then user should be saved correctly")
    public void givenAnUnRegisteredUser_whenRegisterUser_thenUserShouldBeSavedCorrectly() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("HjuIY9jk5op&tc");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(buddyAccountService.saveBuddyAccount(any(BuddyAccount.class))).thenReturn(any(BuddyAccount.class));

        UserDTO userSaved = userService.registerUser(userToSaveDTO);

        assertThat(userSaved).isEqualToComparingFieldByField(userSavedDTO);
        InOrder inOrder = inOrder(userRepository, passwordEncoder, buddyAccountService);
        inOrder.verify(userRepository).findByEmail(anyString());
        inOrder.verify(passwordEncoder).encode(anyString());
        inOrder.verify(userRepository).save(any(User.class));
        inOrder.verify(buddyAccountService).saveBuddyAccount(any(BuddyAccount.class));
    }

    @Test(expected = DataAlreadyRegisteredException.class)
    @Tag("RegisterUser")
    @DisplayName("If user is already registered, when registerUser, then throw DataAlreadyRegisteredException")
    public void givenARegisteredUser_whenRegisterUser_thenDataAlreadyRegisteredExceptionIsThrown() {
        when(userRepository.findByEmail(anyString())).thenReturn(user);

        userService.registerUser(userToSaveDTO);
    }

    @Test
    @Tag("GetUserByEmail")
    @DisplayName("Given an user, when getUserByEmail, then result should match expected user")
    public void givenAnUser_whenGetUserByEmail_thenReturnExpectedUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(user);

        User userFound = userService.getUserByEmail(anyString());

        assertThat(userFound).isEqualToComparingFieldByField(user);
        verify(userRepository).findByEmail(anyString());
    }

    @Test(expected = UsernameNotFoundException.class)
    @Tag("GetUserByEmail")
    @DisplayName("If user is not found, when getUserByEmail, then throw UsernameNotFoundException")
    public void givenAnUnFoundUser_whenGetUserByEmail_thenUsernameNotFoundExceptionIsThrown() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        userService.getUserByEmail(anyString());
    }

    @Test
    @Tag("addConnection")
    @DisplayName("Given a buddy to add, when addConnection, then buddy should be add correctly")
    public void givenABuddyToAdd_whenAddConnection_thenBuddyShouldBeAddCorrectly() {
        user.setContacts(new ArrayList<>(Arrays.asList()));
        when(userRepository.findByEmail("laurahbl@gmail.com")).thenReturn(user);
        when(userRepository.findByEmail("buddy@gmail.com")).thenReturn(buddyToAdd);
        when(userRepository.save(any(User.class))).thenReturn(user);

        ContactsDTO contacts = userService.addConnection("laurahbl@gmail.com","buddy@gmail.com");

        assertThat(contacts.getContacts()).contains("buddy@gmail.com");
        verify(userRepository).save(any(User.class));

    }

    @Test(expected = UsernameNotFoundException.class)
    @Tag("addConnection")
    @DisplayName("If buddy to add is not found, when addConnection, then throw UsernameNotFoundException")
    public void givenAnUnFoundBuddy_whenAddConnection_thenUsernameNotFoundExceptionIsThrown() {
        when(userRepository.findByEmail("laurahbl@gmail.com")).thenReturn(user);
        when(userRepository.findByEmail("buddy@gmail.com")).thenReturn(null);

        userService.addConnection("laurahbl@gmail.com", "buddy@gmail.com");
    }

    @Test(expected = DataAlreadyRegisteredException.class)
    @Tag("addConnection")
    @DisplayName("If buddy is already in contact, when addConnection, then throw DataAlreadyRegisteredException")
    public void givenAnAlreadyAddedBuddy_whenAddConnection_thenDataAlreadyRegisteredExceptionIsThrown() {
        user.setContacts(Arrays.asList(buddyToAdd));
        when(userRepository.findByEmail("laurahbl@gmail.com")).thenReturn(user);
        when(userRepository.findByEmail("buddy@gmail.com")).thenReturn(buddyToAdd);
        userService.addConnection("laurahbl@gmail.com", "buddy@gmail.com");
    }
}
