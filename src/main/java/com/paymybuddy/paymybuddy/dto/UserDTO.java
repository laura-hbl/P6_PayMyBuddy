package com.paymybuddy.paymybuddy.dto;

import com.sun.istack.NotNull;
import javax.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class UserDTO {

    private static final int FIRST_NAME_MAX_SIZE = 70;

    private static final int LAST_NAME_MAX_SIZE = 70;

    private static final int EMAIL_MAX_SIZE = 70;

    private static final int PASSWORD_MIN_SIZE = 5;

    private static final int PHONE_MAX_SIZE = 20;

    @NotNull
    @NotEmpty(message = "First name is required")
    @Length(max = EMAIL_MAX_SIZE)
    private String firstName;

    @NotNull
    @NotEmpty(message = "Last name is required")
    @Length(max = EMAIL_MAX_SIZE)
    private String lastName;

    @NotNull
    @NotEmpty(message = "Email address is required")
    @Length(max = EMAIL_MAX_SIZE, message = "Please enter a valid email address")
    private String email;

    @NotNull
    @NotEmpty(message = "Password is required")
    @Length(min = 5, message = "The Password you provided must have at least 5 characters")
    private String password;

    @NotNull
    @NotEmpty(message = "Phone number is required")
    @Length(max = PHONE_MAX_SIZE, message = "Please enter a valid phone number")
    private String phone;
}
