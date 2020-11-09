package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.model.BuddyAccount;

/**
 * BuddyAccountService interface.
 *
 * @author Laura Habdul
 */
public interface IBuddyAccountService {

    /**
     * Saves the given buddy account on database.
     *
     * @param buddyAccount the buddyAccount to be saved
     * @return The buddyAccount saved
     */
    BuddyAccount saveBuddyAccount(final BuddyAccount buddyAccount);
}
