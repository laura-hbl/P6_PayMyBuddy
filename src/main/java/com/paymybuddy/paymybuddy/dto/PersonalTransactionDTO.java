package com.paymybuddy.paymybuddy.dto;

import java.math.BigDecimal;

public class PersonalTransactionDTO {

    private String email;

    private String description;

    private BigDecimal amount;

    public PersonalTransactionDTO(String email, String description, BigDecimal amount) {
        this.email = email;
        this.description = description;
        this.amount = amount;
    }

    public PersonalTransactionDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
