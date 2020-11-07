package com.paymybuddy.paymybuddy.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class ContactsDTO {

    private Collection<String> contacts = new ArrayList<>();
}
