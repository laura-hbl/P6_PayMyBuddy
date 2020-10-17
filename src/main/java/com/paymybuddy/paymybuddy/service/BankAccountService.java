package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.dto.BankAccountDTO;
import com.paymybuddy.paymybuddy.dto.BankAccountInfoDTO;
import com.paymybuddy.paymybuddy.exception.DataAlreadyRegisteredException;
import com.paymybuddy.paymybuddy.model.BankAccount;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService implements IBankAccountService {

    private BankAccountRepository bankAccountRepository;

    private IUserService userService;

    @Autowired
    public BankAccountService(final BankAccountRepository bankAccountRepository, final UserService userService) {
        this.bankAccountRepository = bankAccountRepository;
        this.userService = userService;
    }

    public BankAccountDTO createBankAccount(BankAccountInfoDTO bankAccountInfoDTO) {

        User user = userService.getUserByEmail(bankAccountInfoDTO.getEmail());

        if (user.getBankAccount() != null) {
            throw new DataAlreadyRegisteredException("Bank Account is already registered");
        }

        BankAccount bankAccount = bankAccountRepository.save(new BankAccount(user, bankAccountInfoDTO.getIban(),
                bankAccountInfoDTO.getBic()));

        return new BankAccountDTO(bankAccount.getIban(), bankAccount.getBic());
    }

}
