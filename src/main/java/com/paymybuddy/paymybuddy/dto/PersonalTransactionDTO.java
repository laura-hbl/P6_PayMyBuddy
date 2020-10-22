package com.paymybuddy.paymybuddy.dto;

import com.sun.istack.NotNull;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class PersonalTransactionDTO {

    private static final int DESCRIPTION_MAX_SIZE = 100;

    @NotNull
    @NotEmpty(message = "Email is required")
    private String email;

    @NotNull
    @NotEmpty(message = "Description is required")
    @Length(max = DESCRIPTION_MAX_SIZE, message = "Description size must have less than 100 characters")
    private String description;

    @NotNull
    @DecimalMax(value = "999.99", message = "maximum amount authorized is 999.99")
    @DecimalMin(value = "1.00", message = "minimum amount authorized is 1")
    private BigDecimal amount;
}
