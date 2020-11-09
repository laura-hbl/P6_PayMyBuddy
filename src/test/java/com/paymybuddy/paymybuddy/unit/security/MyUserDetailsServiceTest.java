package com.paymybuddy.paymybuddy.unit.security;

import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import com.paymybuddy.paymybuddy.security.MyUserDetailsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MyUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    private User owner;

    @Before
    public void setUp() {
        owner = new User("Laura", "Habdul", "laurahbl@gmail.com", "HjuIY9jk5op&tc",
                "0601331013");
    }

    @Test
    @Tag("LoadUserByUsername")
    @DisplayName("Given an user, when loadUserByUsername, then return correct user details")
    public void givenAnUser_whenLoadUserByUsername_thenUserDetailsShouldBeReturnedCorrectly() {
        when(userRepository.findByEmail(anyString())).thenReturn(owner);

        UserDetails userDetails = myUserDetailsService.loadUserByUsername("laurahbl@gmail.com");

        assertThat(userDetails.getUsername()).isEqualTo("laurahbl@gmail.com");
        assertThat(userDetails.getPassword()).isEqualTo("HjuIY9jk5op&tc");
        verify(userRepository).findByEmail(anyString());
    }

    @Test(expected = UsernameNotFoundException.class)
    @Tag("LoadUserByUsername")
    @DisplayName("If user is not registered, when loadUserByUsername, then throw UsernameNotFoundException")
    public void givenAnUnFoundUser_whenLoadUserByUsername_thenUsernameNotFoundExceptionIsThrown() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        myUserDetailsService.loadUserByUsername("laurahbl@gmail.com");
    }
}
