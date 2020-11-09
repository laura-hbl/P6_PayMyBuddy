package com.paymybuddy.paymybuddy.model;

import javax.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * Permits the storage and retrieving data of an bank account.
 *
 * @author Laura Habdul
 */
@Entity
@Table(name = "bank_account")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class BankAccount implements Serializable {

    /**
     * Id of bank_account table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    /**
     * User whose bank account belong to.
     */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    /**
     * IBAN code of bank account.
     */
    @Column(name = "iban")
    private String iban;

    /**
     * BIC code of bank account.
     */
    @Column(name = "bic")
    private String bic;

    /**
     * Constructor of class BankAccount.
     * Initialize owner, iban and bic.
     *
     * @param owner User whose bank account belong to
     * @param iban  IBAN code of bank account
     * @param bic   BIC code of bank account
     */
    public BankAccount(final User owner, final String iban, final String bic) {
        super();
        this.owner = owner;
        this.iban = iban;
        this.bic = bic;
    }
}
