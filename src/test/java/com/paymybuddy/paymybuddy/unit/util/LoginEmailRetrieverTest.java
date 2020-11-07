package com.paymybuddy.paymybuddy.unit.util;

import com.paymybuddy.paymybuddy.util.LoginEmailRetriever;
import javax.servlet.http.HttpServletRequest;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginEmailRetrieverTest {

    @Mock
    private HttpServletRequest mockedRequest;

    @Mock
    private Principal principal;

    @InjectMocks
    LoginEmailRetriever loginEmailRetriever;

    @Test
    @DisplayName("Given an username, when getUsername, then correct username should be returned")
    public void givenAnUsername_whenGetUsername_thenReturnExpectedUsername() {

        when(mockedRequest.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn("foo@gmail.com");

        String username = loginEmailRetriever.getUsername(mockedRequest);

        assertThat(username).isEqualTo("foo@gmail.com");
    }
}
