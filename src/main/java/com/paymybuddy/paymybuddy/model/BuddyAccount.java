package com.paymybuddy.paymybuddy.model;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

@Entity
@Table(name = "buddy_account")
public class BuddyAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @OneToMany(mappedBy = "buddyOwner", fetch = FetchType.LAZY)
    private Collection<Transaction> transactionSenders;

    @OneToMany(mappedBy = "buddyReceiver", fetch = FetchType.LAZY)
    private Collection<Transaction> transactionReceivers;

    public BuddyAccount() {
    }

    public BuddyAccount(User owner, BigDecimal balance) {
        super();
        this.owner = owner;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Collection<Transaction> getTransactionSenders() {
        return transactionSenders;
    }

    public void setTransactionSenders(Collection<Transaction> transactionSenders) {
        this.transactionSenders = transactionSenders;
    }

    public Collection<Transaction> getTransactionReceivers() {
        return transactionReceivers;
    }

    public void setTransactionReceivers(Collection<Transaction> transactionReceivers) {
        this.transactionReceivers = transactionReceivers;
    }
}
