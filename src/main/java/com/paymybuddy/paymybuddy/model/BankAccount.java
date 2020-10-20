package com.paymybuddy.paymybuddy.model;

import javax.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "bank_account")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class BankAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @Column(name = "iban")
    private String iban;

    @Column(name = "bic")
    private String bic;

    public BankAccount(User owner, String iban, String bic) {
        super();
        this.owner = owner;
        this.iban = iban;
        this.bic = bic;
    }
}
