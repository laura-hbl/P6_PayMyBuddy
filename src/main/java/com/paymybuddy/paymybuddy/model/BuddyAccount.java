package com.paymybuddy.paymybuddy.model;

import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Permits the storage and retrieving data of an buddy account.
 *
 * @author Laura Habdul
 */
@Entity
@Table(name = "buddy_account")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class BuddyAccount implements Serializable {

    /**
     * Id of buddy_account table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    /**
     * User whose this buddy account belongs to.
     */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    /**
     * The credit balance of the Buddy account.
     */
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    /**
     * Constructor of class BuddyAccount.
     * Initialize owner and balance.
     *
     * @param owner   user whose buddy account belong to
     * @param balance the credit balance of Buddy account
     */
    public BuddyAccount(final User owner, final BigDecimal balance) {
        super();
        this.owner = owner;
        this.balance = balance;
    }
}
