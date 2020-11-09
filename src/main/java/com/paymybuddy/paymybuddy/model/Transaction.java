package com.paymybuddy.paymybuddy.model;

import javax.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Permits the storage and retrieving data of an transaction.
 *
 * @author Laura Habdul
 */
@Entity
@Table(name = "transaction")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class Transaction implements Serializable {

    /**
     * Id of transaction table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    /**
     * The type of transaction.
     */
    @Getter
    @Setter
    private String type;

    /**
     * Buddy account of sender
     */
    @ManyToOne
    @JoinColumn(name = "buddy_account_owner_id")
    private BuddyAccount buddyOwner;

    /**
     * Buddy account of receiver
     */
    @ManyToOne
    @JoinColumn(name = "buddy_account_receiver_id")
    private BuddyAccount buddyReceiver;

    /**
     * Bank account of sender
     */
    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;

    /**
     * Email of the buddy.
     */
    @Column(name = "date", nullable = false)
    private LocalDate date;

    /**
     * Description of transaction.
     */
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * Amount of transaction.
     */
    @Column(name = "amount")
    private BigDecimal amount;

    /**
     * Fee calculated of transaction.
     */
    @Column(name = "fee")
    private BigDecimal fee;

    /**
     * Constructor of class TransactionDTO.
     * Initialize type, buddyEmail, address, date, description, amount and fee.
     *
     * @param type          type of transaction (payment)
     * @param buddyOwner    buddy account of sender
     * @param buddyReceiver buddy account of receiver
     * @param date          date of transaction
     * @param description   description of transaction
     * @param amount        amount of transaction
     * @param fee           fee calculated of transaction
     */
    public Transaction(final String type, final BuddyAccount buddyOwner, final BuddyAccount buddyReceiver,
                       final LocalDate date, final String description, final BigDecimal amount, final BigDecimal fee) {
        super();
        this.type = type;
        this.buddyOwner = buddyOwner;
        this.buddyReceiver = buddyReceiver;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.fee = fee;
    }

    /**
     * Constructor of class TransactionDTO.
     * Initialize type, buddyEmail, address, date, description, amount and fee.
     *
     * @param type        type of transaction (transfer or recharge)
     * @param buddyOwner  buddy account of owner
     * @param bankAccount bank account of owner
     * @param date        date of transaction
     * @param description description of transaction
     * @param amount      amount of transaction
     * @param fee         fee calculated of transaction
     */
    public Transaction(final String type, final BuddyAccount buddyOwner, final BankAccount bankAccount,
                       final LocalDate date, final String description, final BigDecimal amount, final BigDecimal fee) {
        super();
        this.type = type;
        this.buddyOwner = buddyOwner;
        this.bankAccount = bankAccount;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.fee = fee;
    }
}
