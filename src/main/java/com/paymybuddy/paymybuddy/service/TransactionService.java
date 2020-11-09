package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.constants.TransactionType;
import com.paymybuddy.paymybuddy.dto.PaymentTransactionDTO;
import com.paymybuddy.paymybuddy.dto.PersonalTransactionDTO;
import com.paymybuddy.paymybuddy.dto.TransactionDTO;
import com.paymybuddy.paymybuddy.exception.BadRequestException;
import com.paymybuddy.paymybuddy.exception.ResourceNotFoundException;
import com.paymybuddy.paymybuddy.model.BuddyAccount;
import com.paymybuddy.paymybuddy.model.Transaction;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.paymybuddy.util.FeeCalculator;
import javax.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Contains methods that allow interaction between Transaction business logic and Transaction repository.
 *
 * @author Laura Habdul
 * @see ITransactionService
 * @see IBuddyAccountService
 * @see IUserService
 */
@Service
public class TransactionService implements ITransactionService {

    /**
     * TransactionService logger.
     */
    private static final Logger LOGGER = LogManager.getLogger(TransactionService.class);

    /**
     * TransactionRepository instance.
     */
    private final TransactionRepository transactionRepository;

    /**
     * IUserService's implement class reference.
     */
    private final IUserService userService;

    /**
     * IBuddyAccountService's implement class reference.
     */
    private final IBuddyAccountService buddyAccountService;

    /**
     * FeeCalculator instance.
     */
    private final FeeCalculator feeCalculator;

    /**
     * Constructor of class TransactionService.
     * Initialize transactionRepository, userService, buddyAccountService and feeCalculator.
     *
     * @param transactionRepository TransactionRepository instance
     * @param userService           IUserService's implement class reference
     * @param buddyAccountService   IBuddyAccountService's implement class reference
     * @param feeCalculator         FeeCalculator instance
     */
    @Autowired
    public TransactionService(final TransactionRepository transactionRepository, final IUserService userService,
                              final IBuddyAccountService buddyAccountService, final FeeCalculator feeCalculator) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.buddyAccountService = buddyAccountService;
        this.feeCalculator = feeCalculator;
    }

    /**
     * Calls UserService's getUserByEmail method to retrieves the user with the given email and checks if user has
     * registered a bank account, if not ResourceNotFoundException is thrown. Calculates transaction fee by calling
     * FeeCalculator's get fee method and checks if there is sufficient funds on balance for the transfer, if not
     * BadRequestException is thrown. Else, updates balance then save buddy account and transaction to database.
     *
     * @param ownerEmail the user email
     * @param transfer   PersonalTransactionDTO instance that contains transfer information (amount, description)
     * @return The transaction saved converted to a TransactionDTO object
     */
    @Transactional
    public TransactionDTO transferToBankAccount(final String ownerEmail, final PersonalTransactionDTO transfer) {
        LOGGER.debug("Inside TransactionService.transferToBankAccount for {}", ownerEmail);
        User user = userService.getUserByEmail(ownerEmail);

        if (user.getBankAccount() == null) {
            throw new ResourceNotFoundException("You need to register your Bank Account to transfer money");
        }
        BuddyAccount buddyAccount = user.getBuddyAccount();
        BigDecimal balance = buddyAccount.getBalance();
        BigDecimal amount = transfer.getAmount();

        // Calculates the transaction fee.
        BigDecimal fee = feeCalculator.getFee(amount).setScale(2, RoundingMode.HALF_UP);

        // Checks if there is sufficient funds on balance for the transfer.
        if (balance.compareTo(amount.add(fee)) < 0) {
            throw new BadRequestException("Insufficient funds on your balance for this transfer!");
        }
        // Updates balance by subtracting amount and fee.
        buddyAccount.setBalance(balance.subtract(amount.add(fee)));
        BuddyAccount buddyAccountSaved = buddyAccountService.saveBuddyAccount(buddyAccount);

        Transaction transaction = transactionRepository.save(new Transaction(TransactionType.TRANSFER,
                buddyAccountSaved, user.getBankAccount(), LocalDate.now(), transfer.getDescription(), amount, fee));

        TransactionDTO transactionDTO = new TransactionDTO(transaction.getType(), transaction.getBuddyOwner().getOwner()
                .getEmail(), transaction.getDate(), transaction.getDescription(), transaction.getAmount(),
                transaction.getFee());

        return transactionDTO;
    }


    /**
     * Calls UserService's getUserByEmail method to retrieves the user with the given email and checks if user has
     * registered a bank account, if not ResourceNotFoundException is thrown. Else, calculates transaction fee by
     * calling FeeCalculator's get fee method. Updates balance then save buddy account and transaction to database.
     *
     * @param ownerEmail the user email
     * @param recharge   PersonalTransactionDTO instance that contains recharge information (amount, description)
     * @return The transaction saved converted to a TransactionDTO object
     */
    @Transactional
    public TransactionDTO rechargeBalance(final String ownerEmail, final PersonalTransactionDTO recharge) {
        LOGGER.debug("Inside TransactionService.rechargeBalance for {}", ownerEmail);
        User user = userService.getUserByEmail(ownerEmail);

        if (user.getBankAccount() == null) {
            throw new ResourceNotFoundException("You need to register your Bank Account to recharge balance");
        }
        BuddyAccount buddyAccount = user.getBuddyAccount();
        BigDecimal balance = buddyAccount.getBalance();
        BigDecimal amount = recharge.getAmount();

        // Calculates the transaction fee.
        BigDecimal fee = feeCalculator.getFee(amount).setScale(2, RoundingMode.HALF_UP);

        // Updates balance by subtracting amount and fee.
        buddyAccount.setBalance(balance.subtract(fee).add(amount));
        buddyAccountService.saveBuddyAccount(buddyAccount);

        Transaction transaction = transactionRepository.save(new Transaction(TransactionType.RECHARGE, buddyAccount,
                user.getBankAccount(), LocalDate.now(), recharge.getDescription(), amount, fee));

        TransactionDTO transactionDTO = new TransactionDTO(transaction.getType(), transaction.getBuddyOwner().getOwner()
                .getEmail(), transaction.getDate(), transaction.getDescription(), transaction.getAmount(),
                transaction.getFee());

        return transactionDTO;
    }


    /**
     * Calls UserService's getUserByEmail method to retrieves the sender and receiver and checks if receiver is
     * registered in sender contacts, if not ResourceNotFoundException is thrown. Calculates transaction fee by calling
     * FeeCalculator's get fee method and checks if there is sufficient funds on balance for the payment, if not
     * BadRequestException is thrown. Else, updates sender and receiver balance then saves their buddy account and
     * transaction to database.
     *
     * @param ownerEmail the sender email
     * @param payment    PaymentTransactionDTO instance that contains payment info (receiver email, amount, description)
     * @return The transaction saved converted to a TransactionDTO object
     */
    @Transactional
    public TransactionDTO payMyBuddy(final String ownerEmail, final PaymentTransactionDTO payment) {
        LOGGER.debug("Inside TransactionService.payMyBuddy");
        User sender = userService.getUserByEmail(ownerEmail);
        User receiver = userService.getUserByEmail(payment.getBuddyEmail());

        // Checks if receiver is registered in sender contacts.
        if (!sender.getContacts().contains(receiver)) {
            throw new ResourceNotFoundException("This buddy is not in your contacts. Please add to contacts for payment");
        }
        BuddyAccount senderBuddyAccount = sender.getBuddyAccount();
        BigDecimal senderBalance = senderBuddyAccount.getBalance();
        BigDecimal amount = payment.getAmount();

        // Calculates the transaction fee.
        BigDecimal fee = feeCalculator.getFee(amount).setScale(2, RoundingMode.HALF_UP);

        // Checks if there is sufficient funds on sender balance for the payment.
        if (senderBalance.compareTo(amount.add(fee)) < 0) {
            throw new BadRequestException("Insufficient funds for this transfer. Please recharge your balance!");
        }
        // Updates sender balance by subtracting amount and fee.
        senderBuddyAccount.setBalance(senderBalance.subtract(amount.add(fee)));
        buddyAccountService.saveBuddyAccount(senderBuddyAccount);

        BuddyAccount receiverBuddyAccount = receiver.getBuddyAccount();
        // Updates receiver balance by adding amount.
        receiverBuddyAccount.setBalance(receiverBuddyAccount.getBalance().add(amount));
        buddyAccountService.saveBuddyAccount(receiverBuddyAccount);

        Transaction transaction = transactionRepository.save(new Transaction(TransactionType.PAYMENT,
                senderBuddyAccount, receiverBuddyAccount, LocalDate.now(), payment.getDescription(), amount, fee));

        TransactionDTO transactionDTO = new TransactionDTO(transaction.getType(), transaction.getBuddyReceiver()
                .getOwner().getEmail(), transaction.getDate(), transaction.getDescription(), transaction.getAmount(),
                transaction.getFee());

        return transactionDTO;
    }
}
