package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.dto.BankAccountDTO;
import com.paymybuddy.paymybuddy.exception.DataAlreadyRegisteredException;
import com.paymybuddy.paymybuddy.model.BankAccount;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService implements IBankAccountService {

    private final BankAccountRepository bankAccountRepository;

    private final IUserService userService;

    @Autowired
    public BankAccountService(final BankAccountRepository bankAccountRepository, final UserService userService) {
        this.bankAccountRepository = bankAccountRepository;
        this.userService = userService;
    }

    public BankAccountDTO createBankAccount(BankAccountDTO bankAccountDTO) {

        User user = userService.getUserByEmail(bankAccountDTO.getEmail());

        if (user.getBankAccount() != null) {
            throw new DataAlreadyRegisteredException("Your bank Account is already registered");
        }

        BankAccount bankAccount = new BankAccount(user, bankAccountDTO.getIban(), bankAccountDTO.getBic());
        BankAccount bankAccountSaved = bankAccountRepository.save(bankAccount);

        return new BankAccountDTO(bankAccountSaved.getOwner().getEmail(), bankAccountSaved.getIban(),
                bankAccountSaved.getBic());
    }

}
