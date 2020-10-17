package com.paymybuddy.paymybuddy.dto;

import java.math.BigDecimal;

public class PaymentTransactionDTO {

    private String senderEmail;

    private String receiverEmail;

    private String description;

    private BigDecimal amount;

    public PaymentTransactionDTO(final String senderEmail, final String receiverEmail, final String description,
                                 final BigDecimal amount) {
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.description = description;
        this.amount = amount;
    }

    public PaymentTransactionDTO() {
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
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
