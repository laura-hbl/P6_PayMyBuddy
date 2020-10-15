package com.paymybuddy.paymybuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "phone", nullable = false)
    private String phone;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "owner")
    private BuddyAccount buddyAccount;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "owner")
    private BankAccount bankAccount;

    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name = "connect", joinColumns = @JoinColumn(name = "buddy_id"), inverseJoinColumns = @JoinColumn(name = "owner_id"))
    private Collection<User> contacts;

    public User() {
    }

    public User(String firstName, String lastName, String email, String password, String phone) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public BuddyAccount getBuddyAccount() {
        return buddyAccount;
    }

    public void setBuddyAccount(BuddyAccount buddyAccount) {
        this.buddyAccount = buddyAccount;
    }

    public Collection<User> getContacts() {
        return contacts;
    }

    public void setContacts(Collection<User> contacts) {
        this.contacts = contacts;
    }
}
