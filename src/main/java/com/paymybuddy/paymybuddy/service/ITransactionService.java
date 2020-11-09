package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.dto.PaymentTransactionDTO;
import com.paymybuddy.paymybuddy.dto.PersonalTransactionDTO;
import com.paymybuddy.paymybuddy.dto.TransactionDTO;

/**
 * TransactionService interface.
 *
 * @author Laura Habdul
 */
public interface ITransactionService {

    /**
     * Transfers money from application to bank account.
     *
     * @param ownerEmail the user email
     * @param transfer   PersonalTransactionDTO instance that contains transfer information (amount and description)
     * @return The transaction saved converted to a TransactionDTO object
     */
    TransactionDTO transferToBankAccount(final String ownerEmail, final PersonalTransactionDTO transfer);

    /**
     * Recharges the app balance by transferring money from bank account.
     *
     * @param ownerEmail the user email
     * @param recharge   PersonalTransactionDTO instance that contains recharge information (amount and description)
     * @return The transaction saved converted to a TransactionDTO object
     */
    TransactionDTO rechargeBalance(final String ownerEmail, final PersonalTransactionDTO recharge);

    /**
     * Pays a buddy on application.
     *
     * @param ownerEmail the sender email
     * @param payment    PaymentTransactionDTO instance that contains payment info (receiver email, amount, description)
     * @return The transaction saved converted to a TransactionDTO object
     */
    TransactionDTO payMyBuddy(final String ownerEmail, final PaymentTransactionDTO payment);
}
