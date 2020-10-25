package com.paymybuddy.paymybuddy.dto;

import com.paymybuddy.paymybuddy.constants.Constraints;
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
public class PaymentTransactionDTO {

    @NotNull
    @NotEmpty(message = "Buddy email is required")
    private String buddyEmail;

    @NotNull
    @NotEmpty(message = "Description is required")
    @Length(max = Constraints.DESCRIPTION_MAX_SIZE, message = "Description size must have less than 100 characters")
    private String description;

    @NotNull
    @DecimalMax(value = "999.99", message = "maximum amount authorized is 999.99")
    @DecimalMin(value = "1.00", message = "minimum amount authorized is 1" )
    private BigDecimal amount;
}
