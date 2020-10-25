package com.paymybuddy.paymybuddy.service;

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
import java.time.LocalDate;

@Service
public class TransactionService implements ITransactionService {

    private static final Logger LOGGER = LogManager.getLogger(TransactionService.class);

    private final TransactionRepository transactionRepository;

    private final IUserService userService;

    private final IBuddyAccountService buddyAccountService;

    private final FeeCalculator feeCalculator;


    @Autowired
    public TransactionService(final TransactionRepository transactionRepository, final IUserService userService,
                              final IBuddyAccountService buddyAccountService, final FeeCalculator feeCalculator) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.buddyAccountService = buddyAccountService;
        this.feeCalculator = feeCalculator;
    }

    @Transactional
    public TransactionDTO transferToBankAccount(String ownerEmail, PersonalTransactionDTO transferInfo) {
        LOGGER.debug("Inside TransactionService.transferToBankAccount for {}", ownerEmail);
        User user = userService.getUserByEmail(ownerEmail);

        if (user.getBankAccount() == null) {
            throw new ResourceNotFoundException("You need to register your Bank Account to transfer money");
        }
        BuddyAccount buddyAccount = user.getBuddyAccount();

        BigDecimal balance = buddyAccount.getBalance();
        BigDecimal amount = transferInfo.getAmount();
        BigDecimal fee = feeCalculator.getFee(amount);

        if (balance.compareTo(amount.add(fee)) < 0) {
            throw new BadRequestException("Insufficient funds on your balance for this transfer!");
        }
        buddyAccount.setBalance(balance.subtract(amount.add(fee)));
        buddyAccountService.saveBuddyAccount(buddyAccount);

        Transaction transaction = transactionRepository.save(new Transaction(buddyAccount, user.getBankAccount(),
                LocalDate.now(), transferInfo.getDescription(), amount, fee));

        TransactionDTO transactionDTO = new TransactionDTO(transaction.getBuddyOwner().getOwner().getEmail(),
                transaction.getDate(), transaction.getDescription(), transaction.getAmount(), transaction.getFee());

        return transactionDTO;
    }

    @Transactional
    public TransactionDTO rechargeBalance(String ownerEmail, PersonalTransactionDTO rechargeInfo) {
        LOGGER.debug("Inside TransactionService.rechargeBalance for {}", ownerEmail);
        User user = userService.getUserByEmail(ownerEmail);

        if (user.getBankAccount() == null) {
            throw new ResourceNotFoundException("You need to register your Bank Account to recharge balance");
        }
        BuddyAccount buddyAccount = user.getBuddyAccount();
        BigDecimal balance = buddyAccount.getBalance();
        BigDecimal amount = rechargeInfo.getAmount();

        BigDecimal fee = feeCalculator.getFee(amount);
        buddyAccount.setBalance(balance.subtract(fee).add(amount));
        buddyAccountService.saveBuddyAccount(buddyAccount);

        Transaction transaction = transactionRepository.save(new Transaction(buddyAccount, user.getBankAccount(),
                LocalDate.now(), rechargeInfo.getDescription(), amount, fee));

        TransactionDTO transactionDTO = new TransactionDTO(transaction.getBuddyOwner().getOwner().getEmail(),
                transaction.getDate(), transaction.getDescription(), transaction.getAmount(), transaction.getFee());

        return transactionDTO;
    }

    @Transactional
    public TransactionDTO payMyBuddy(String ownerEmail, PaymentTransactionDTO paymentInfo) {
        LOGGER.debug("Inside TransactionService.payMyBuddy");
        User sender = userService.getUserByEmail(ownerEmail);
        User receiver = userService.getUserByEmail(paymentInfo.getBuddyEmail());

        if (!sender.getContacts().contains(receiver)) {
            throw new ResourceNotFoundException("Fail to get connection. Please check the email entered");
        }
        BuddyAccount senderBuddyAccount = sender.getBuddyAccount();
        BigDecimal senderBalance = senderBuddyAccount.getBalance();
        BigDecimal amount = paymentInfo.getAmount();

        BigDecimal fee = feeCalculator.getFee(amount);

        if (senderBalance.compareTo(amount.add(fee)) < 0) {
            throw new BadRequestException("Insufficient funds for this transfer. Please recharge your balance!");
        }
        senderBuddyAccount.setBalance(senderBalance.subtract(amount.add(fee)));
        buddyAccountService.saveBuddyAccount(senderBuddyAccount);

        BuddyAccount receiverBuddyAccount = receiver.getBuddyAccount();
        receiverBuddyAccount.setBalance(receiverBuddyAccount.getBalance().add(amount));
        buddyAccountService.saveBuddyAccount(receiverBuddyAccount);

        Transaction transaction = transactionRepository.save(new Transaction(senderBuddyAccount, receiverBuddyAccount,
                LocalDate.now(), paymentInfo.getDescription(), amount, fee));

        TransactionDTO transactionDTO = new TransactionDTO(transaction.getBuddyReceiver().getOwner().getEmail(),
                transaction.getDate(), transaction.getDescription(), transaction.getAmount(), transaction.getFee());

        return transactionDTO;
    }
}
