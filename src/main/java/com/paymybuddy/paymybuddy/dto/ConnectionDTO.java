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
public class ConnectionDTO {

    @NotNull
    @NotEmpty(message = "Buddy Email address is required")
    @Length(min = Constraints.EMAIL_MIN_SIZE, message = "Please enter a valid email address")
    private String buddyEmail;
}
