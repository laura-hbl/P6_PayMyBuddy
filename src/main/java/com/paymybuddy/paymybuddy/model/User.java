package com.paymybuddy.paymybuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@ToString
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "phone")
    private String phone;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "owner")
    private BuddyAccount buddyAccount;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "owner")
    private BankAccount bankAccount;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "connect", joinColumns = @JoinColumn(name = "buddy_id"),
            inverseJoinColumns = @JoinColumn(name = "owner_id"))
    private Collection<User> contacts;


    public User(String firstName, String lastName, String email, String password, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }
}
