package com.paymybuddy.paymybuddy.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Permits the storage and retrieving of the list of contact of an user.
 *
 * @author Laura Habdul
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class ContactsDTO {

    /**
     * List of contact.
     */
    private Collection<String> contacts = new ArrayList<>();
}
