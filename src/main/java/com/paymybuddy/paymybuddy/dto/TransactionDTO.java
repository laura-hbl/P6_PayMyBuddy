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

    private String ownerEmail;

    private String buddyEmail;

    private LocalDate date;

    private String description;

    private BigDecimal amount;

    private BigDecimal fee;

    public TransactionDTO(String ownerEmail, String buddyEmail, LocalDate date, String description, BigDecimal amount,
                          BigDecimal fee) {
        this.ownerEmail = ownerEmail;
        this.buddyEmail = buddyEmail;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.fee = fee;
    }

    public TransactionDTO(String ownerEmail, LocalDate date, String description, BigDecimal amount, BigDecimal fee) {
        this.ownerEmail = ownerEmail;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.fee = fee;
    }
}