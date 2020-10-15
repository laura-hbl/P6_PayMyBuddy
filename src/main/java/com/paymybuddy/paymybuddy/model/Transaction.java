package com.paymybuddy.paymybuddy.model;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transaction")
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buddy_account_owner_id")
    private BuddyAccount buddyOwner;

    @ManyToOne
    @JoinColumn(name = "buddy_account_receiver_id")
    private BuddyAccount buddyReceiver;

    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "fee", nullable = false)
    private BigDecimal fee;

    public Transaction() {
    }

    public Transaction(BuddyAccount buddyOwner, BuddyAccount buddyReceiver, BankAccount bankAccount, LocalDate date,
                       String description, BigDecimal amount, BigDecimal fee) {
        super();
        this.buddyOwner = buddyOwner;
        this.buddyReceiver = buddyReceiver;
        this.bankAccount = bankAccount;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.fee = fee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BuddyAccount getBuddyOwner() {
        return buddyOwner;
    }

    public void setBuddyOwner(BuddyAccount buddyOwner) {
        this.buddyOwner = buddyOwner;
    }

    public BuddyAccount getBuddyReceiver() {
        return buddyReceiver;
    }

    public void setBuddyReceiver(BuddyAccount buddyReceiver) {
        this.buddyReceiver = buddyReceiver;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
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
