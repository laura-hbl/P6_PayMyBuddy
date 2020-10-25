package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.dto.BankAccountDTO;
import com.paymybuddy.paymybuddy.exception.DataAlreadyRegisteredException;
import com.paymybuddy.paymybuddy.model.BankAccount;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.BankAccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService implements IBankAccountService {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountService.class);

    private final BankAccountRepository bankAccountRepository;

    private final IUserService userService;

    @Autowired
    public BankAccountService(final BankAccountRepository bankAccountRepository, final UserService userService) {
        this.bankAccountRepository = bankAccountRepository;
        this.userService = userService;
    }

    public BankAccountDTO createBankAccount(String ownerEmail, BankAccountDTO bankAccountDTO) {
        LOGGER.debug("Inside BankAccountService.createBankAccount for username : " + ownerEmail);
        User user = userService.getUserByEmail(ownerEmail);

        if (user.getBankAccount() != null) {
            throw new DataAlreadyRegisteredException("Your bank Account is already registered");
        }
        BankAccount bankAccount = bankAccountRepository.save(new BankAccount(user, bankAccountDTO.getIban(),
                bankAccountDTO.getBic()));

        return new BankAccountDTO(bankAccount.getIban(), bankAccount.getBic());
    }
}
