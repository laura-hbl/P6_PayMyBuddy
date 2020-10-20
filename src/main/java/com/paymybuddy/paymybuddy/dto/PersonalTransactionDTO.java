package com.paymybuddy.paymybuddy.dto;

import com.sun.istack.NotNull;
import javax.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class PersonalTransactionDTO {

    private static final int DESCRIPTION_MAX_SIZE = 100;

    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    @Length(min = 1, max = DESCRIPTION_MAX_SIZE)
    private String description;

    @NotNull
    @NumberFormat
    private BigDecimal amount;
}
