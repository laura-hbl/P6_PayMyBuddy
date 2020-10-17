package com.paymybuddy.paymybuddy.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

/**
 * Contain methods that handles exceptions across the whole application.
 *
 * @author Laura Habdul
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     *  GlobalExceptionHandler logger.
     */
    private static final Logger LOGGER = LogManager.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles exception of the specific type DataAlreadyRegisteredException.
     *
     * @param ex DataAlreadyRegisteredException object
     * @param request WebRequest object
     * @return ResponseEntity error response object and Http Status generated
     */
    @ExceptionHandler(DataAlreadyRegisteredException.class)
    public ResponseEntity handleConflict(final DataAlreadyRegisteredException ex, final WebRequest request) {
        LOGGER.error("Request - FAILED :", ex);
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity handleUnauthorized(final UsernameNotFoundException ex, final WebRequest request) {
        LOGGER.error("Request - FAILED :", ex);
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity handleBadCredentials(final BadCredentialsException ex, final WebRequest request) {
        LOGGER.error("Request - FAILED :", ex);
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity handleBadRequest(final BadRequestException ex, final WebRequest request) {
        LOGGER.error("Request - FAILED :", ex);
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity handleNotFound(final ResourceNotFoundException ex, final WebRequest request) {
        LOGGER.error("Request - FAILED :", ex);
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}