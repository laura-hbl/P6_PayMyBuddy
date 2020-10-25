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

@RestController
public class BankAccountController {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountController.class);

    private final IBankAccountService bankAccountService;

    private final LoginEmailRetriever loginEmailRetriever;

    @Autowired
    public BankAccountController(final IBankAccountService bankAccountService,
                                 final LoginEmailRetriever loginEmailRetriever) {
        this.bankAccountService = bankAccountService;
        this.loginEmailRetriever = loginEmailRetriever;
    }

    @PostMapping("/bankAccount")
    public ResponseEntity<BankAccountDTO> addBankAccount(@Valid @RequestBody final BankAccountDTO bankAccountDTO,
                                                         HttpServletRequest request) {
        LOGGER.debug("Bank account POST request with username {}", request.getUserPrincipal().getName());

        String ownerEmail = loginEmailRetriever.getUsername(request);

        BankAccountDTO bankAccount = bankAccountService.createBankAccount(ownerEmail, bankAccountDTO);

        LOGGER.info("Bank account POST request - SUCCESS");
        return new ResponseEntity<>(bankAccount, HttpStatus.CREATED);
    }
}
