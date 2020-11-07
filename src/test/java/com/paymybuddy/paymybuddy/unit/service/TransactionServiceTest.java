package com.paymybuddy.paymybuddy.unit.service;

import com.paymybuddy.paymybuddy.constants.TransactionTypes;
import com.paymybuddy.paymybuddy.dto.PaymentTransactionDTO;
import com.paymybuddy.paymybuddy.dto.PersonalTransactionDTO;
import com.paymybuddy.paymybuddy.dto.TransactionDTO;
import com.paymybuddy.paymybuddy.exception.BadRequestException;
import com.paymybuddy.paymybuddy.exception.ResourceNotFoundException;
import com.paymybuddy.paymybuddy.model.BankAccount;
import com.paymybuddy.paymybuddy.model.BuddyAccount;
import com.paymybuddy.paymybuddy.model.Transaction;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.paymybuddy.service.BuddyAccountService;
import com.paymybuddy.paymybuddy.service.TransactionService;
import com.paymybuddy.paymybuddy.service.UserService;
import com.paymybuddy.paymybuddy.util.FeeCalculator;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private FeeCalculator feeCalculator;

    @Mock
    private BuddyAccountService buddyAccountService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    private static PersonalTransactionDTO personalTransactionDTO;

    private static PaymentTransactionDTO paymentTransactionDTO;

    private static User owner;

    private static User buddy;

    private static Transaction transfer;

    private static Transaction recharge;

    private static Transaction payment;

    private static BankAccount bankAccount;

    private static BuddyAccount ownerAccount;

    private static BuddyAccount buddyAccount;

    private static TransactionDTO transactionDTO;

    @Before
    public void setUp() {
        owner = new User("Laura", "Habdul", "laurahbl@gmail.com", "HjuIY9jk5o",
                "0601331013");
        bankAccount = new BankAccount(owner, "099NJKKOKLJIMK4", "09878KOLJ");
        ownerAccount = new BuddyAccount(owner, BigDecimal.valueOf(100));
        owner.setBankAccount(bankAccount);
        owner.setBuddyAccount(ownerAccount);

        buddy = new User("Buddy", "Buddy", "buddy@gmail.com", "dr2@tde8èftKhY",
                "0607978866");
        buddyAccount = new BuddyAccount(buddy, BigDecimal.valueOf(100));
        buddy.setBuddyAccount(buddyAccount);

        owner.setContacts(Arrays.asList(buddy));

        transfer = new Transaction(TransactionTypes.TRANSFER, ownerAccount, bankAccount, LocalDate.now(),
                "transfer", BigDecimal.valueOf(10), BigDecimal.valueOf(0.05));

        recharge = new Transaction(TransactionTypes.RECHARGE, ownerAccount, bankAccount, LocalDate.now(),
                "recharge", BigDecimal.valueOf(10), BigDecimal.valueOf(0.05));

        payment = new Transaction(TransactionTypes.PAYMENT, ownerAccount, buddyAccount, LocalDate.now(),
                "food", BigDecimal.valueOf(10), BigDecimal.valueOf(0.05));
    }

    @Test
    @Tag("TransferToBankAccount")
    @DisplayName("Given PersonalTransaction , when transferToBankAccount, then transfer should be done in correct order" +
            " and return expected transaction")
    public void givenAPersonalTransaction_whenTransferToBankAccount_thenReturnExpectedTransaction() {
        personalTransactionDTO = new PersonalTransactionDTO("transfer", BigDecimal.valueOf(10));
        transactionDTO = new TransactionDTO(TransactionTypes.TRANSFER, "laurahbl@gmail.com", LocalDate.now(),
                "transfer", BigDecimal.valueOf(10), BigDecimal.valueOf(0.05));
        when(userService.getUserByEmail(anyString())).thenReturn(owner);
        when(feeCalculator.getFee(any(BigDecimal.class))).thenReturn(BigDecimal.valueOf(0.05));
        when(buddyAccountService.saveBuddyAccount(any(BuddyAccount.class))).thenReturn(ownerAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transfer);

        TransactionDTO transferSaved = transactionService.transferToBankAccount("laurahbl@gmail.com",
                personalTransactionDTO);

        assertThat(transferSaved).isEqualToComparingFieldByField(transactionDTO);
        InOrder inOrder = inOrder(userService, feeCalculator, buddyAccountService, transactionRepository);
        inOrder.verify(userService).getUserByEmail(anyString());
        inOrder.verify(feeCalculator).getFee(any(BigDecimal.class));
        inOrder.verify(buddyAccountService).saveBuddyAccount(any(BuddyAccount.class));
        inOrder.verify(transactionRepository).save(any(Transaction.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    @Tag("TransferToBankAccount")
    @DisplayName("If user has not registered a bank account, when transferToBankAccount, then throw ResourceNotFoundException")
    public void givenANullBankAccount_whenTransferToBankAccount_thenResourceNotFoundExceptionIsThrown() {
        owner.setBankAccount(null);
        when(userService.getUserByEmail(anyString())).thenReturn(owner);
        personalTransactionDTO = new PersonalTransactionDTO("transfer", BigDecimal.valueOf(30));

        transactionService.transferToBankAccount("laurahbl@gmail.com", personalTransactionDTO);
    }

    @Test(expected = BadRequestException.class)
    @Tag("TransferToBankAccount")
    @DisplayName("If amount + fee is greater than balance, when transferToBankAccount, then throw BadRequestException")
    public void givenInsufficientFunds_whenTransferToBankAccount_thenBadRequestExceptionIsThrown() {
        personalTransactionDTO = new PersonalTransactionDTO("transfer", BigDecimal.valueOf(100));
        when(userService.getUserByEmail(anyString())).thenReturn(owner);
        when(feeCalculator.getFee(any(BigDecimal.class))).thenReturn(BigDecimal.valueOf(0.5));

        transactionService.transferToBankAccount("laurahbl@gmail.com", personalTransactionDTO);
    }

    @Test
    @Tag("RechargeBalance")
    @DisplayName("Given personalTransaction, when rechargeBalance, then recharge should be done in correct order and " +
            "return expected transaction")
    public void givenPersonalTransaction_whenRechargeBalance_thenReturnExpectedTransaction() {
        personalTransactionDTO = new PersonalTransactionDTO("recharge", BigDecimal.valueOf(10));
        transactionDTO = new TransactionDTO(TransactionTypes.RECHARGE, "laurahbl@gmail.com", LocalDate.now(),
                "recharge", BigDecimal.valueOf(10), BigDecimal.valueOf(0.05));
        when(userService.getUserByEmail(anyString())).thenReturn(owner);
        when(feeCalculator.getFee(any(BigDecimal.class))).thenReturn(BigDecimal.valueOf(0.05));
        when(buddyAccountService.saveBuddyAccount(any(BuddyAccount.class))).thenReturn(ownerAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(recharge);

        TransactionDTO rechargeSaved = transactionService.rechargeBalance("laurahbl@gmail.com",
                personalTransactionDTO);

        assertThat(rechargeSaved).isEqualToComparingFieldByField(transactionDTO);
        InOrder inOrder = inOrder(userService, feeCalculator, buddyAccountService, transactionRepository);
        inOrder.verify(userService).getUserByEmail(anyString());
        inOrder.verify(feeCalculator).getFee(any(BigDecimal.class));
        inOrder.verify(buddyAccountService).saveBuddyAccount(any(BuddyAccount.class));
        inOrder.verify(transactionRepository).save(any(Transaction.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    @Tag("RechargeBalance")
    @DisplayName("If user has not registered a bank account, when rechargeBalance, then throw ResourceNotFoundException")
    public void givenANullBankAccount_whenRechargeBalance_thenResourceNotFoundExceptionIsThrown() {
        owner.setBankAccount(null);
        when(userService.getUserByEmail(anyString())).thenReturn(owner);
        personalTransactionDTO = new PersonalTransactionDTO("recharge", BigDecimal.valueOf(50));

        transactionService.rechargeBalance("laurahbl@gmail.com", personalTransactionDTO);
    }

    @Test
    @Tag("PayMyBuddy")
    @DisplayName("Given paymentTransaction, when payMyBuddy, then payment should be done in correct order and return " +
            "expected transaction")
    public void givenPaymentTransaction_whenPayMyBuddy_thenReturnExpectedTransaction() {
        paymentTransactionDTO = new PaymentTransactionDTO("buddy@gmail.com", "food",
                BigDecimal.valueOf(10));
        transactionDTO = new TransactionDTO(TransactionTypes.PAYMENT, "buddy@gmail.com", LocalDate.now(),
                "food", BigDecimal.valueOf(10), BigDecimal.valueOf(0.05));
        when(userService.getUserByEmail("laurahbl@gmail.com")).thenReturn(owner);
        when(userService.getUserByEmail("buddy@gmail.com")).thenReturn(buddy);
        when(feeCalculator.getFee(any(BigDecimal.class))).thenReturn(BigDecimal.valueOf(0.05));
        when(buddyAccountService.saveBuddyAccount(ownerAccount)).thenReturn(ownerAccount);
        when(buddyAccountService.saveBuddyAccount(buddyAccount)).thenReturn(buddyAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(payment);

        TransactionDTO paymentSaved = transactionService.payMyBuddy("laurahbl@gmail.com",
                paymentTransactionDTO);

        assertThat(paymentSaved).isEqualToComparingFieldByField(transactionDTO);
        InOrder inOrder = inOrder(userService, feeCalculator, buddyAccountService, transactionRepository);
        inOrder.verify(userService, times(2)).getUserByEmail(anyString());
        inOrder.verify(feeCalculator).getFee(any(BigDecimal.class));
        inOrder.verify(buddyAccountService, times(2)).saveBuddyAccount(any(BuddyAccount.class));
        inOrder.verify(transactionRepository).save(any(Transaction.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    @Tag("PayMyBuddy")
    @DisplayName("If buddy to pay is not in owner contact, when payMyBuddy, then throw ResourceNotFoundException")
    public void givenUnFoundBuddyInContact_whenPayMyBuddy_thenResourceNotFoundExceptionIsThrown() {
        owner.setContacts(Arrays.asList());
        paymentTransactionDTO = new PaymentTransactionDTO("buddy@gmail.com", "food",
                BigDecimal.valueOf(10));
        when(userService.getUserByEmail("laurahbl@gmail.com")).thenReturn(owner);
        when(userService.getUserByEmail("buddy@gmail.com")).thenReturn(buddy);

        transactionService.payMyBuddy("laurahbl@gmail.com", paymentTransactionDTO);
    }

    @Test(expected = BadRequestException.class)
    @Tag("PayMyBuddy")
    @DisplayName("If funds is insufficient for payment, when payMyBuddy, then throw BadRequestException")
    public void givenInsufficientFunds_whenPayMyBuddy_thenBadRequestExceptionIsThrown() {
        paymentTransactionDTO = new PaymentTransactionDTO("buddy@gmail.com", "food",
                BigDecimal.valueOf(100));
        when(userService.getUserByEmail("laurahbl@gmail.com")).thenReturn(owner);
        when(userService.getUserByEmail("buddy@gmail.com")).thenReturn(buddy);
        when(feeCalculator.getFee(any(BigDecimal.class))).thenReturn(BigDecimal.valueOf(0.05));

        transactionService.payMyBuddy("laurahbl@gmail.com", paymentTransactionDTO);
    }
}
