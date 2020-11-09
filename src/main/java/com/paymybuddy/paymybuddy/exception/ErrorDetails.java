package com.paymybuddy.paymybuddy.exception;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Permits the storage and retrieving of customized error response.
 *
 * @author Laura Habdul
 */
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class ErrorDetails {

    private LocalDateTime timestamp;
    private String message;
    private String details;
}
