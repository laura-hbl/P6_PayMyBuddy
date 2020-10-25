package com.paymybuddy.paymybuddy.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class TransactionDTO {

    private String email;

    private LocalDate date;

    private String description;

    private BigDecimal amount;

    private BigDecimal fee;

    public TransactionDTO(String buddyEmail, LocalDate date, String description, BigDecimal amount,
                          BigDecimal fee) {
        this.email = buddyEmail;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.fee = fee;
    }

    public TransactionDTO(LocalDate date, String description, BigDecimal amount, BigDecimal fee) {
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.fee = fee;
    }
}