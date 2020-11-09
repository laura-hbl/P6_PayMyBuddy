package com.paymybuddy.paymybuddy.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Permits the storage and retrieving data of an completed transaction.
 *
 * @author Laura Habdul
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class TransactionDTO {

    /**
     * The type of transaction.
     */
    private String type;

    /**
     * Email of owner (if personal transaction) / buddy (if payment).
     */
    private String email;

    /**
     * Date of transaction
     */
    private LocalDate date;

    /**
     * Description of transaction.
     */
    private String description;

    /**
     * Amount of transaction.
     */
    private BigDecimal amount;

    /**
     * Fee calculated of transaction.
     */
    private BigDecimal fee;
}