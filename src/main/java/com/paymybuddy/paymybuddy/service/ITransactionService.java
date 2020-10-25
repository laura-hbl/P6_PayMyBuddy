package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.dto.PaymentTransactionDTO;
import com.paymybuddy.paymybuddy.dto.PersonalTransactionDTO;
import com.paymybuddy.paymybuddy.dto.TransactionDTO;

public interface ITransactionService {

    TransactionDTO transferToBankAccount(String ownerEmail, PersonalTransactionDTO transfer);

    TransactionDTO rechargeBalance(String ownerEmail, PersonalTransactionDTO recharge);

    TransactionDTO payMyBuddy(String ownerEmail, PaymentTransactionDTO payment);
}
