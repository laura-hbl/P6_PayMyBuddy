package com.paymybuddy.paymybuddy.dto;

import com.sun.istack.NotNull;
import javax.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class BankAccountDTO {

    private static final int IBAN_MIN_SIZE = 14;

    private static final int IBAN_MAX_SIZE = 34;

    private static final int BIC_MIN_SIZE = 8;

    private static final int BIC_MAX_SIZE = 11;

    @NotNull
    @NotEmpty(message = "Email is required")
    private String email;

    @NotNull
    @NotEmpty(message = "IBAN code is required")
    @Length(min = IBAN_MIN_SIZE, max = IBAN_MAX_SIZE, message = "Please enter a valid IBAN code")
    private String iban;

    @NotNull
    @NotEmpty(message = "BIC code is required")
    @Length(min = BIC_MIN_SIZE, max = BIC_MAX_SIZE, message = "Please enter a valid BIC code")
    private String bic;
}
