package com.paymybuddy.paymybuddy.dto;

import com.paymybuddy.paymybuddy.constants.Constraints;
import com.sun.istack.NotNull;
import javax.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class UserDTO {

    @NotNull
    @NotEmpty(message = "First name is required")
    @Length(max = Constraints.EMAIL_MAX_SIZE)
    private String firstName;

    @NotNull
    @NotEmpty(message = "Last name is required")
    @Length(max = Constraints.EMAIL_MAX_SIZE)
    private String lastName;

    @NotNull
    @NotEmpty(message = "Email address is required")
    @Length(max = Constraints.EMAIL_MAX_SIZE, message = "Please enter a valid email address")
    private String email;

    @NotNull
    @NotEmpty(message = "Password is required")
    @Length(min = Constraints.PASSWORD_MIN_SIZE, message = "The Password you provided must have at least 5 characters")
    private String password;

    @NotNull
    @NotEmpty(message = "Phone number is required")
    @Length(max = Constraints.PHONE_MAX_SIZE, message = "Please enter a valid phone number")
    private String phone;
}
