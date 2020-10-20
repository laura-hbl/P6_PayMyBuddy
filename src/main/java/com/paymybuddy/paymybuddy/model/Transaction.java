package com.paymybuddy.paymybuddy.model;

import javax.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transaction")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
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

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "fee")
    private BigDecimal fee;

    public Transaction(BuddyAccount buddyOwner, BuddyAccount buddyReceiver, LocalDate date, String description, BigDecimal amount, BigDecimal fee) {
        super();
        this.buddyOwner = buddyOwner;
        this.buddyReceiver = buddyReceiver;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.fee = fee;
    }

    public Transaction(BuddyAccount buddyOwner, BankAccount bankAccount, LocalDate date, String description,
                       BigDecimal amount, BigDecimal fee) {
        super();
        this.buddyOwner = buddyOwner;
        this.bankAccount = bankAccount;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.fee = fee;
    }
}
