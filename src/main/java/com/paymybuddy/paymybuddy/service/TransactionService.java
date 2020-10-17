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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;

@Service
public class TransactionService implements ITransactionService {

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
    public TransactionDTO transferToBankAccount(PersonalTransactionDTO transferInfo) {

        User user = userService.getUserByEmail(transferInfo.getEmail());

        if (user.getBankAccount() == null) {
            throw new ResourceNotFoundException("You need to register your Bank Account to transfer money");
        }

        BuddyAccount buddyAccount = user.getBuddyAccount();
        BigDecimal balance = buddyAccount.getBalance();
        BigDecimal amount = transferInfo.getAmount();

        BigDecimal fee = feeCalculator.getFee(amount);

        if (balance.compareTo(amount.add(fee)) < 0) {
            throw new BadRequestException("Insufficient funds for this transfer !");
        }

        buddyAccount.setBalance(balance.subtract(amount.add(fee)));
        Transaction transaction = new Transaction(user.getBuddyAccount(), user.getBankAccount(), LocalDate.now(),
                transferInfo.getDescription(), transferInfo.getAmount(), fee);

        buddyAccountService.saveBuddyAccount(buddyAccount);
        Transaction transfer = transactionRepository.save(transaction);

        return new TransactionDTO(transfer.getBuddyOwner().getOwner().getEmail(), transfer.getDate(),
                transfer.getDescription(), transfer.getAmount(), transfer.getFee());
    }

    @Transactional
    public TransactionDTO rechargeBalance(PersonalTransactionDTO rechargeInfo) {

        User user = userService.getUserByEmail(rechargeInfo.getEmail());

        if (user.getBankAccount() == null) {
            throw new ResourceNotFoundException("You need to register your Bank Account to recharge balance");
        }

        BuddyAccount buddyAccount = user.getBuddyAccount();
        BigDecimal balance = buddyAccount.getBalance();
        BigDecimal amount = rechargeInfo.getAmount();

        BigDecimal fee = feeCalculator.getFee(amount);

        if (amount.equals(BigDecimal.ZERO)) {
            throw new BadRequestException(" Invalid Input! Please enter a valid amount!");
        }

        buddyAccount.setBalance(balance.subtract(fee).add(amount));
        Transaction transaction = new Transaction(user.getBuddyAccount(), user.getBankAccount(), LocalDate.now(),
                rechargeInfo.getDescription(), rechargeInfo.getAmount(), fee);

        buddyAccountService.saveBuddyAccount(buddyAccount);
        Transaction recharge = transactionRepository.save(transaction);

        return new TransactionDTO(recharge.getBuddyOwner().getOwner().getEmail(),  recharge.getDate(),
                recharge.getDescription(), recharge.getAmount(), recharge.getFee());
    }

    @Transactional
    public TransactionDTO payMyBuddy(PaymentTransactionDTO paymentInfo) {

        User sender = userService.getUserByEmail(paymentInfo.getSenderEmail());
        User receiver = userService.getConnection(paymentInfo.getSenderEmail(), paymentInfo.getReceiverEmail());

        BuddyAccount senderBuddyAccount = sender.getBuddyAccount();
        BuddyAccount receiverBuddyAccount = receiver.getBuddyAccount();

        BigDecimal senderBalance = senderBuddyAccount.getBalance();
        BigDecimal receiverBalance = receiverBuddyAccount.getBalance();

        BigDecimal amount = paymentInfo.getAmount();
        BigDecimal fee = feeCalculator.getFee(amount);

        if (senderBalance.compareTo(amount.add(fee)) < 0) {
            throw new BadRequestException("Insufficient funds for this transfer. Please recharge your balance!");
        }

        Transaction transaction = new Transaction(sender.getBuddyAccount(), receiver.getBuddyAccount(), LocalDate.now(),
                paymentInfo.getDescription(), paymentInfo.getAmount(), fee);

        senderBuddyAccount.setBalance(senderBalance.subtract(amount.add(fee)));
        Collection<Transaction> transactionReceiver = senderBuddyAccount.getTransactionReceivers();
        transactionReceiver.add(transaction);

        receiverBuddyAccount.setBalance(receiverBalance.add(amount));
        Collection<Transaction> transactionSender = senderBuddyAccount.getTransactionSenders();
        transactionSender.add(transaction);

        buddyAccountService.saveBuddyAccount(senderBuddyAccount);
        buddyAccountService.saveBuddyAccount(receiverBuddyAccount);

        Transaction payment = transactionRepository.save(transaction);

        return new TransactionDTO(payment.getBuddyOwner().getOwner().getEmail(),
                payment.getBuddyReceiver().getOwner().getEmail(), payment.getDate(), payment.getDescription(),
                payment.getAmount(), payment.getFee());
    }
}
