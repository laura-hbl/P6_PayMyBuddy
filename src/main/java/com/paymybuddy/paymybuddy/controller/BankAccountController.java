package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.dto.BankAccountDTO;
import com.paymybuddy.paymybuddy.service.IBankAccountService;
import com.paymybuddy.paymybuddy.util.LoginEmailRetriever;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Creates REST endpoints for operations on bank account data.
 *
 * @author Laura Habdul
 * @see IBankAccountService
 */
@RestController
public class BankAccountController {

    /**
     * BankAccountController logger.
     */
    private static final Logger LOGGER = LogManager.getLogger(BankAccountController.class);

    /**
     * IBankAccountService's implement class reference.
     */
    private final IBankAccountService bankAccountService;

    /**
     * LoginEmailRetriever instance.
     */
    private final LoginEmailRetriever loginEmailRetriever;

    /**
     * Constructor of class BankAccountController.
     * Initialize bankAccountService, loginEmailRetriever.
     *
     * @param bankAccountService  IBankAccountService's implement class reference.
     * @param loginEmailRetriever LoginEmailRetriever instance.
     */
    @Autowired
    public BankAccountController(final IBankAccountService bankAccountService,
                                 final LoginEmailRetriever loginEmailRetriever) {
        this.bankAccountService = bankAccountService;
        this.loginEmailRetriever = loginEmailRetriever;
    }

    /**
     * Adds a bank account on application.
     *
     * @param bankAccountDTO bank account info (IBAN and BIC)
     * @param request        HttpServletRequest instance
     * @return ResponseEntity<BankAccountDTO> The response object and Http Status generated
     */
    @PostMapping("/bankAccount")
    public ResponseEntity<BankAccountDTO> addBankAccount(@Valid @RequestBody final BankAccountDTO bankAccountDTO,
                                                         final HttpServletRequest request) {
        LOGGER.debug("Bank account POST request with IBAN {} and BIC {}", bankAccountDTO.getIban(),
                bankAccountDTO.getBic());

        String ownerEmail = loginEmailRetriever.getUsername(request);

        BankAccountDTO bankAccount = bankAccountService.createBankAccount(ownerEmail, bankAccountDTO);

        LOGGER.info("Bank account POST request - SUCCESS");
        return new ResponseEntity<>(bankAccount, HttpStatus.CREATED);
    }
}
