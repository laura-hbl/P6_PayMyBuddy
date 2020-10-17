package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.dto.BankAccountDTO;
import com.paymybuddy.paymybuddy.dto.BankAccountInfoDTO;

public interface IBankAccountService {

    BankAccountDTO createBankAccount(BankAccountInfoDTO bankAccountInfoDTO);

}
