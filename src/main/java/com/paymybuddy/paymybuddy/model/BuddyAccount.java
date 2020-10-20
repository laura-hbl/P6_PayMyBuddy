package com.paymybuddy.paymybuddy.model;

import javax.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

@Entity
@Table(name = "buddy_account")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
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
    private Collection<Transaction> senders;

    @OneToMany(mappedBy = "buddyReceiver", fetch = FetchType.LAZY)
    private Collection<Transaction> receivers;

    public BuddyAccount(User owner, BigDecimal balance) {
        super();
        this.owner = owner;
        this.balance = balance;
    }
}
