package com.paymybuddy.paymybuddy.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getBuddyEmail() {
        return buddyEmail;
    }

    public void setBuddyEmail(String buddyEmail) {
        this.buddyEmail = buddyEmail;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }
}
