package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.dto.BankAccountDTO;

/**
 * BankAccountService interface.
 *
 * @author Laura Habdul
 */
public interface IBankAccountService {

    /**
     * Creates a bank account for an user in order to make personal transaction (transfer and recharge).
     *
     * @param ownerEmail     the user's email
     * @param bankAccountDTO the bank account to be saved
     * @return The bank account saved converted to a BankAccountDTO object
     */
    BankAccountDTO createBankAccount(final String ownerEmail, final BankAccountDTO bankAccountDTO);
}
