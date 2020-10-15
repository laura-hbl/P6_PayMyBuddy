package com.paymybuddy.paymybuddy.model;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "bank_account")
public class BankAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name="user_id", nullable = false)
    private User owner;

    @Column(name = "iban")
    private String iban;

    @Column(name = "bic")
    private String bic;

    public BankAccount() {
    }

    public BankAccount(User owner, String iban, String bic) {
        super();
        this.owner = owner;
        this.iban = iban;
        this.bic = bic;
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

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }
}
