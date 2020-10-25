package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.dto.PaymentTransactionDTO;
import com.paymybuddy.paymybuddy.dto.PersonalTransactionDTO;
import com.paymybuddy.paymybuddy.dto.TransactionDTO;
import com.paymybuddy.paymybuddy.exception.BadRequestException;
import com.paymybuddy.paymybuddy.service.ITransactionService;
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
public class TransactionController {

    private static final Logger LOGGER = LogManager.getLogger(TransactionController.class);

    private final ITransactionService transactionService;

    private final LoginEmailRetriever loginEmailRetriever;

    @Autowired
    public TransactionController(final ITransactionService transactionService,
                                 final LoginEmailRetriever loginEmailRetriever) {
        this.transactionService = transactionService;
        this.loginEmailRetriever = loginEmailRetriever;
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionDTO> transferToBankAccount(@Valid @RequestBody final PersonalTransactionDTO
                                                                        transferInfo, HttpServletRequest request) {
        LOGGER.debug("Transfer request with username {}", request.getUserPrincipal().getName());

        String ownerEmail = loginEmailRetriever.getUsername(request);

        if (transferInfo.getAmount() == null || transferInfo.getAmount().toString().trim().length() == 0) {
            throw new BadRequestException("Amount is required");
        }
        TransactionDTO transferSaved = transactionService.transferToBankAccount(ownerEmail, transferInfo);

        LOGGER.info("Money transfer request - SUCCESS");
        return new ResponseEntity<>(transferSaved, HttpStatus.OK);
    }

    @PostMapping("/recharge")
    public ResponseEntity<TransactionDTO> rechargeBalance(@Valid @RequestBody final PersonalTransactionDTO rechargeInfo,
                                                          HttpServletRequest request) {
        LOGGER.debug("Recharge request with username {}", request.getUserPrincipal().getName());

        String ownerEmail = loginEmailRetriever.getUsername(request);

        if (rechargeInfo.getAmount() == null || rechargeInfo.getAmount().toString().trim().length() == 0) {
            throw new BadRequestException("Amount is required");
        }
        TransactionDTO rechargeSaved = transactionService.rechargeBalance(ownerEmail, rechargeInfo);

        LOGGER.info("Balance recharge request - SUCCESS");
        return new ResponseEntity<>(rechargeSaved, HttpStatus.OK);
    }

    @PostMapping("/payment")
    public ResponseEntity<TransactionDTO> payment(@Valid @RequestBody final PaymentTransactionDTO paymentInfo,
                                                  HttpServletRequest request) {
        LOGGER.debug("Payment request from {} to {}", request.getUserPrincipal().getName(),
                paymentInfo.getBuddyEmail());

        String ownerEmail = loginEmailRetriever.getUsername(request);

        if (paymentInfo.getAmount() == null || paymentInfo.getAmount().toString().trim().length() == 0) {
            throw new BadRequestException("Amount is required");
        }
        TransactionDTO paymentSaved = transactionService.payMyBuddy(ownerEmail, paymentInfo);

        LOGGER.info("Payment request - SUCCESS");
        return new ResponseEntity<>(paymentSaved, HttpStatus.OK);
    }
}
