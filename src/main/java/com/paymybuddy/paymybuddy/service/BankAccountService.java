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

/**
 * Contains methods that allow interaction between BankAccount business logic and BankAccount repository.
 *
 * @author Laura Habdul
 * @see IBankAccountService
 * @see IUserService
 */
@Service
public class BankAccountService implements IBankAccountService {

    /**
     * BankAccountService logger.
     */
    private static final Logger LOGGER = LogManager.getLogger(BankAccountService.class);

    /**
     * BankAccountRepository instance.
     */
    private final BankAccountRepository bankAccountRepository;

    /**
     * IUserService's implement class reference.
     */
    private final IUserService userService;

    /**
     * Constructor of class BankAccountService.
     * Initialize bankAccountRepository, userService.
     *
     * @param bankAccountRepository BankAccountRepository instance
     * @param userService           IUserService's implement class reference
     */
    @Autowired
    public BankAccountService(final BankAccountRepository bankAccountRepository, final UserService userService) {
        this.bankAccountRepository = bankAccountRepository;
        this.userService = userService;
    }

    /**
     * Calls UserService's getUserByEmail method to retrieves the user with the given email and checks if a bank account
     * is already registered, if so DataAlreadyRegisteredException is thrown. Else, the given bankAccountDTO object is
     * converted to model object and then saved by calling BankAccountRepository's save method.
     *
     * @param ownerEmail     the user's email
     * @param bankAccountDTO the bank account to be saved
     * @return The bank account saved converted to a BankAccountDTO object
     */
    public BankAccountDTO createBankAccount(final String ownerEmail, final BankAccountDTO bankAccountDTO) {
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
