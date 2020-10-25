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
public class BankAccountDTO {

    @NotNull
    @NotEmpty(message = "IBAN code is required")
    @Length(min = Constraints.IBAN_MIN_SIZE, max = Constraints.IBAN_MAX_SIZE, message = "Please enter a valid IBAN code")
    private String iban;

    @NotNull
    @NotEmpty(message = "BIC code is required")
    @Length(min = Constraints.BIC_MIN_SIZE, max = Constraints.BIC_MAX_SIZE, message = "Please enter a valid BIC code")
    private String bic;
}
