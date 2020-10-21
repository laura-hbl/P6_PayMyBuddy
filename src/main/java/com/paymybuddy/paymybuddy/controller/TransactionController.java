package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.dto.PaymentTransactionDTO;
import com.paymybuddy.paymybuddy.dto.PersonalTransactionDTO;
import com.paymybuddy.paymybuddy.dto.TransactionDTO;
import com.paymybuddy.paymybuddy.service.ITransactionService;
import com.paymybuddy.paymybuddy.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    private final ITransactionService transactionService;

    private final Validator validator;

    @Autowired
    public TransactionController(final ITransactionService transactionService, final Validator validator) {
        this.transactionService = transactionService;
        this.validator = validator;
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionDTO> transferToBankAccount(@RequestBody final PersonalTransactionDTO transferInfo) {

        validator.validate(transferInfo);

        TransactionDTO transferSaved = transactionService.transferToBankAccount(transferInfo);

        return new ResponseEntity<>(transferSaved, HttpStatus.OK);
    }

    @PostMapping("/recharge")
    public ResponseEntity<TransactionDTO> rechargeBalance(@RequestBody final PersonalTransactionDTO rechargeInfo) {

        validator.validate(rechargeInfo);

        TransactionDTO rechargeSaved = transactionService.rechargeBalance(rechargeInfo);

        return new ResponseEntity<>(rechargeSaved, HttpStatus.OK);
    }

    @PostMapping("/payment")
    public ResponseEntity<TransactionDTO> payment(@RequestBody final PaymentTransactionDTO paymentInfo) {

        validator.validate(paymentInfo);

        TransactionDTO paymentSaved = transactionService.payMyBuddy(paymentInfo);

        return new ResponseEntity<>(paymentSaved, HttpStatus.OK);
    }
}
