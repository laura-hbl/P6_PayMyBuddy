package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.dto.PaymentTransactionDTO;
import com.paymybuddy.paymybuddy.dto.PersonalTransactionDTO;
import com.paymybuddy.paymybuddy.dto.TransactionDTO;

public interface ITransactionService {

    TransactionDTO transferToBankAccount(PersonalTransactionDTO transfer);

    TransactionDTO rechargeBalance(PersonalTransactionDTO recharge);

    TransactionDTO payMyBuddy(PaymentTransactionDTO payment);
}
