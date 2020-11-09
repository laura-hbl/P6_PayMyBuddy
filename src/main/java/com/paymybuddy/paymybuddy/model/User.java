package com.paymybuddy.paymybuddy.model;

import javax.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Collection;

/**
 * Permits the storage and retrieving data of an user.
 *
 * @author Laura Habdul
 */
@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@ToString
public class User implements Serializable {

    /**
     * Id of user table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    /**
     * First name of the user.
     */
    @Column(name = "first_name")
    private String firstName;

    /**
     * Last name of the user.
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * Email of the user.
     */
    @Column(name = "email", unique = true)
    private String email;

    /**
     * Password of the user.
     */
    @Column(name = "password")
    private String password;

    /**
     * Phone number of the user.
     */
    @Column(name = "phone")
    private String phone;

    /**
     * Buddy account of the user.
     */
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "owner")
    private BuddyAccount buddyAccount;

    /**
     * Bank account of the user.
     */
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "owner")
    private BankAccount bankAccount;

    /**
     * Contacts of the user.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "connect", joinColumns = @JoinColumn(name = "owner_id"),
            inverseJoinColumns = @JoinColumn(name = "buddy_id"))
    private Collection<User> contacts;

    /**
     * Constructor of class User.
     * Initialize firstName, lastName, email, password and phone.
     *
     * @param firstName first name of the user
     * @param lastName  last name of the user
     * @param email     email of the user
     * @param password  password of the user
     * @param phone     phone number of the user
     */
    public User(final String firstName, final String lastName, final String email, final String password,
                final String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }
}
