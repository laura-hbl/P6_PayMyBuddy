package com.paymybuddy.paymybuddy.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;

/**
 * Application security configuration class.
 *
 * @author Laura Habdul
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * UserDetailsService instance.
     */
    private final UserDetailsService userDetailsService;

    /**
     * ObjectMapper instance.
     */
    private final ObjectMapper objectMapper;

    /**
     * Constructor of class SecurityConfiguration.
     * Initialize userDetailsService and objectMapper.
     *
     * @param userDetailsService UserDetailsService instance
     * @param objectMapper       ObjectMapper instance
     */
    public SecurityConfiguration(final UserDetailsService userDetailsService, final ObjectMapper objectMapper) {
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    /**
     * Creates an instance of BCryptPasswordEncoder in order to encrypt the password.
     *
     * @return PasswordEncoder instance
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure the AuthenticationManagerBuilder to use the password encoder.
     *
     * @param authManagerBuilder AuthenticationManagerBuilder instance
     */
    protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
        authManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * Allows configuring web based security for specific http requests.
     *
     * @param http HttpSecurity
     */
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable()
                .requestCache().disable()
                .authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/user").hasAnyRole("USER", "ADMIN")
                .antMatchers("/login", "/registration").permitAll()
                .anyRequest()
                .authenticated()
                .and().formLogin()
                .loginProcessingUrl("/login") //the URL on which the clients should post the login information
                .successHandler(this::loginSuccessHandler)
                .failureHandler(this::loginFailureHandler)
                .and()
                .logout()
                .logoutUrl("/logout") //the URL on which the clients should post if they want to logout
                .logoutSuccessHandler(this::logoutSuccessHandler)
                .invalidateHttpSession(true)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(this::authenticationEntryPointHandler);
    }

    /**
     * Handles login success.
     *
     * @param response       HttpServletResponse object
     * @param request        HttpServletRequest object
     * @param authentication Authentication object
     */
    private void loginSuccessHandler(final HttpServletRequest request, final HttpServletResponse response,
                                     final Authentication authentication) throws IOException {

        response.setStatus(HttpStatus.OK.value());
        objectMapper.writeValue(response.getWriter(), "You are now logged in to Pay my Buddy!");
    }

    /**
     * Handles login failure.
     *
     * @param response HttpServletResponse object
     * @param request  HttpServletRequest object
     * @param e        AuthenticationException object
     */
    private void loginFailureHandler(final HttpServletRequest request, final HttpServletResponse response,
                                     final AuthenticationException e) throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        objectMapper.writeValue(response.getWriter(), "The email or password you entered is incorrect.");
    }

    /**
     * Handles logout success.
     *
     * @param response       HttpServletResponse object
     * @param request        HttpServletRequest object
     * @param authentication Authentication object
     */
    private void logoutSuccessHandler(final HttpServletRequest request, final HttpServletResponse response,
                                      final Authentication authentication) throws IOException {

        response.setStatus(HttpStatus.OK.value());
        objectMapper.writeValue(response.getWriter(), "You have successfully logged out!");
    }

    /**
     * Handles authenticationEntryPoint.
     *
     * @param response HttpServletResponse object
     * @param request  HttpServletRequest object
     * @param e        AuthenticationException object
     */
    private void authenticationEntryPointHandler(final HttpServletRequest request, final HttpServletResponse response,
                                                 final AuthenticationException e) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        objectMapper.writeValue(response.getWriter(), "You have to logged in first!");
    }
}
