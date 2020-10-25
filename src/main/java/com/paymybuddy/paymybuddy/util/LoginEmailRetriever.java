package com.paymybuddy.paymybuddy.util;

import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class LoginEmailRetriever {

    private static final Logger LOGGER = LogManager.getLogger(LoginEmailRetriever.class);

    public String getUsername(HttpServletRequest request) {

        try {
            String username = request.getUserPrincipal().getName();
            return username;
        } catch (NullPointerException e) {
            throw new UsernameNotFoundException("Authentication required. Please login first");
        }
    }
}
