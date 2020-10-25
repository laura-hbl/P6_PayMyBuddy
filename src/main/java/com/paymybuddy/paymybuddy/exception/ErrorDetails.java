package com.paymybuddy.paymybuddy.exception;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class ErrorDetails {

    private LocalDateTime timestamp;
    private String message;
    private String details;
}
