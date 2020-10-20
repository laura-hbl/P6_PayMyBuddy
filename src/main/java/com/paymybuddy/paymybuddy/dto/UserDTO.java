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

    private static final int EMAIL_MIN_SIZE = 8;

    private static final int EMAIL_MAX_SIZE = 70;

    private static final int PASSWORD_MIN_SIZE = 5;

    private static final int PHONE_MAX_SIZE = 20;

    @NotNull
    @NotEmpty
    @Length(max = EMAIL_MAX_SIZE)
    private String firstName;

    @NotNull
    @NotEmpty
    @Length(max = EMAIL_MAX_SIZE)
    private String lastName;

    @NotNull
    @NotEmpty
    @Length(min = EMAIL_MIN_SIZE, max = EMAIL_MAX_SIZE)
    private String email;

    @NotNull
    @NotEmpty
    @Length(min = 5, message = "Password should be at least 4 characters")
    private String password;

    @NotNull
    @NotEmpty
    @Length(max = PHONE_MAX_SIZE)
    private String phone;
}
