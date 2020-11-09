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

/**
 * Creates REST endpoints for transaction operations.
 *
 * @author Laura Habdul
 * @see ITransactionService
 * @see LoginEmailRetriever
 */
@RestController
public class TransactionController {

    /**
     * TransactionController logger.
     */
    private static final Logger LOGGER = LogManager.getLogger(TransactionController.class);

    /**
     * ITransactionService's implement class reference.
     */
    private final ITransactionService transactionService;

    /**
     * LoginEmailRetriever instance.
     */
    private final LoginEmailRetriever loginEmailRetriever;

    /**
     * Constructor of class TransactionController.
     * Initialize transactionService, loginEmailRetriever.
     *
     * @param transactionService  ITransactionService's implement class reference.
     * @param loginEmailRetriever LoginEmailRetriever instance.
     */
    @Autowired
    public TransactionController(final ITransactionService transactionService,
                                 final LoginEmailRetriever loginEmailRetriever) {
        this.transactionService = transactionService;
        this.loginEmailRetriever = loginEmailRetriever;
    }

    /**
     * Transfers money from app to bank account.
     *
     * @param transfer transfer info (description and amount)
     * @param request  HttpServletRequest instance
     * @return ResponseEntity<TransactionDTO> The response object and Http Status generated
     */
    @PostMapping("/transfer")
    public ResponseEntity<TransactionDTO> transferToBankAccount(@Valid @RequestBody final PersonalTransactionDTO
                                                                        transfer, final HttpServletRequest request) {
        LOGGER.debug("Transfer request with amount {}", transfer.getAmount());

        String ownerEmail = loginEmailRetriever.getUsername(request);

        if (transfer.getAmount() == null || transfer.getAmount().toString().trim().length() == 0) {
            throw new BadRequestException("Amount is required");
        }
        TransactionDTO transferSaved = transactionService.transferToBankAccount(ownerEmail, transfer);

        LOGGER.info("Money transfer request - SUCCESS");
        return new ResponseEntity<>(transferSaved, HttpStatus.OK);
    }

    /**
     * Recharges application's balance from bank account.
     *
     * @param recharge recharge info (description and amount)
     * @param request  HttpServletRequest instance
     * @return ResponseEntity<TransactionDTO> The response object and Http Status generated
     */
    @PostMapping("/recharge")
    public ResponseEntity<TransactionDTO> rechargeBalance(@Valid @RequestBody final PersonalTransactionDTO recharge,
                                                          final HttpServletRequest request) {
        LOGGER.debug("Recharge request with amount {}", recharge.getAmount());

        String ownerEmail = loginEmailRetriever.getUsername(request);

        if (recharge.getAmount() == null || recharge.getAmount().toString().trim().length() == 0) {
            throw new BadRequestException("Amount is required");
        }
        TransactionDTO rechargeSaved = transactionService.rechargeBalance(ownerEmail, recharge);

        LOGGER.info("Balance recharge request - SUCCESS");
        return new ResponseEntity<>(rechargeSaved, HttpStatus.OK);
    }

    /**
     * Pays a buddy.
     *
     * @param payment payment info (buddy email, description and amount)
     * @param request HttpServletRequest instance
     * @return ResponseEntity<TransactionDTO> The response object and Http Status generated
     */
    @PostMapping("/payment")
    public ResponseEntity<TransactionDTO> payment(@Valid @RequestBody final PaymentTransactionDTO payment,
                                                  final HttpServletRequest request) {
        LOGGER.debug("Payment request of amount {} to buddy {}", payment.getAmount(), payment.getBuddyEmail());

        String ownerEmail = loginEmailRetriever.getUsername(request);

        if (payment.getAmount() == null || payment.getAmount().toString().trim().length() == 0) {
            throw new BadRequestException("Amount is required");
        }
        TransactionDTO paymentSaved = transactionService.payMyBuddy(ownerEmail, payment);

        LOGGER.info("Payment request - SUCCESS");
        return new ResponseEntity<>(paymentSaved, HttpStatus.OK);
    }
}
