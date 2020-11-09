package com.paymybuddy.paymybuddy.dto;

import com.paymybuddy.paymybuddy.constants.Constraints;
import com.sun.istack.NotNull;
import javax.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

/**
 * Permits the storage and retrieving data of an bank account.
 *
 * @author Laura Habdul
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class BankAccountDTO {

    /**
     * IBAN code of bank account.
     */
    @NotNull
    @NotEmpty(message = "IBAN code is required")
    @Length(min = Constraints.IBAN_MIN_SIZE, max = Constraints.IBAN_MAX_SIZE, message = "Please enter a valid IBAN code")
    private String iban;

    /**
     * BIC code of bank account.
     */
    @NotNull
    @NotEmpty(message = "BIC code is required")
    @Length(min = Constraints.BIC_MIN_SIZE, max = Constraints.BIC_MAX_SIZE, message = "Please enter a valid BIC code")
    private String bic;
}
