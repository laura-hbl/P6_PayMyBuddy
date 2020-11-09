package com.paymybuddy.paymybuddy.util;

import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Retrieves email of the logged in user.
 *
 * @author Laura Habdul
 */
@Component
public class LoginEmailRetriever {

    /**
     * LoginEmailRetriever logger.
     */
    private static final Logger LOGGER = LogManager.getLogger(LoginEmailRetriever.class);

    /**
     * Calculates fee based on amount and rate.
     *
     * @param request HttpServletRequest instance
     * @return the email of the logged in user
     */
    public String getUsername(final HttpServletRequest request) {
        LOGGER.debug("LoginEmailRetriever.getUsername with username : " + request.getUserPrincipal().getName());

        String username = request.getUserPrincipal().getName();

        return username;
    }
}
