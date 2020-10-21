package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.dto.BankAccountDTO;
import com.paymybuddy.paymybuddy.service.IBankAccountService;
import com.paymybuddy.paymybuddy.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankAccountController {

    private final IBankAccountService bankAccountService;

    private final Validator validator;

    @Autowired
    public BankAccountController(final IBankAccountService bankAccountService, final Validator validator) {
        this.bankAccountService = bankAccountService;
        this.validator = validator;
    }

    @PostMapping("/bankAccount")
    public ResponseEntity<BankAccountDTO> addBankAccount(@RequestBody final BankAccountDTO bankAccountDTO) {

        validator.validate(bankAccountDTO);

        BankAccountDTO bankAccount = bankAccountService.createBankAccount(bankAccountDTO);

        return new ResponseEntity<>(bankAccount, HttpStatus.CREATED);
    }
}
