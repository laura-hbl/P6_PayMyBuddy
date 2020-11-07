package com.paymybuddy.paymybuddy.integration;

import com.paymybuddy.paymybuddy.constants.TransactionTypes;
import com.paymybuddy.paymybuddy.dto.PaymentTransactionDTO;
import com.paymybuddy.paymybuddy.dto.PersonalTransactionDTO;
import com.paymybuddy.paymybuddy.dto.TransactionDTO;
import com.paymybuddy.paymybuddy.exception.BadRequestException;
import com.paymybuddy.paymybuddy.exception.ResourceNotFoundException;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.service.TransactionService;
import com.paymybuddy.paymybuddy.service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource({"/application-test.properties"})
@Sql(scripts = "/schema-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class TransactionServiceIT {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Test
    @Tag("TransferToBankAccount")
    @DisplayName("Given PersonalTransaction , when transferToBankAccount, then transfer is done correctly")
    public void givenAPersonalTransaction_whenTransferToBankAccount_thenTransferIsDoneCorrectly() {
        PersonalTransactionDTO transfer = new PersonalTransactionDTO("transfer", BigDecimal.valueOf(50));
        TransactionDTO transaction = new TransactionDTO(TransactionTypes.TRANSFER, "tom@gmail.com",
                LocalDate.now(), "transfer", BigDecimal.valueOf(50), BigDecimal.valueOf(0.25));

        TransactionDTO transferSaved = transactionService.transferToBankAccount("tom@gmail.com", transfer);
        User tom = userService.getUserByEmail("tom@gmail.com");

        assertThat(transferSaved).isEqualToComparingFieldByField(transaction);
        //balance after transfer = 800 - (50 + 0.25) = 749.75
        assertThat(tom.getBuddyAccount().getBalance()).isEqualTo(BigDecimal.valueOf(749.75));
    }

    @Test(expected = BadRequestException.class)
    @Tag("TransferToBankAccount")
    @DisplayName("If amount + fee is greater than balance, when transferToBankAccount, then throw BadRequestException")
    public void givenInsufficientFunds_whenTransferToBankAccount_thenBadRequestExceptionIsThrown() {
        PersonalTransactionDTO transfer = new PersonalTransactionDTO("transfer", BigDecimal.valueOf(15));

        transactionService.transferToBankAccount("leonardo@gmail.com", transfer);
    }

    @Test
    @Tag("RechargeBalance")
    @DisplayName("Given personalTransaction, when rechargeBalance, then recharge is done correctly")
    public void givenPersonalTransaction_whenRechargeBalance_thenRechargeIsDoneCorrectly() {
        PersonalTransactionDTO recharge = new PersonalTransactionDTO("recharge", BigDecimal.valueOf(75));
        TransactionDTO transaction = new TransactionDTO(TransactionTypes.RECHARGE, "leonardo@gmail.com",
                LocalDate.now(), "recharge", BigDecimal.valueOf(75), BigDecimal.valueOf(0.38));

        TransactionDTO rechargeSaved = transactionService.rechargeBalance("leonardo@gmail.com", recharge);
        User leonardo = userService.getUserByEmail("leonardo@gmail.com");

        assertThat(rechargeSaved).isEqualToComparingFieldByField(transaction);
        //balance after recharge = 15 + 75 - 0.38 = 89.62
        assertThat(leonardo.getBuddyAccount().getBalance()).isEqualTo(BigDecimal.valueOf(89.62));
    }

    @Test(expected = ResourceNotFoundException.class)
    @Tag("RechargeBalance")
    @DisplayName("If user has not registered a bank account, when rechargeBalance, then throw ResourceNotFoundException")
    public void givenANullBankAccount_whenRechargeBalance_thenResourceNotFoundExceptionIsThrown() {
        PersonalTransactionDTO recharge = new PersonalTransactionDTO("recharge", BigDecimal.valueOf(50));

        transactionService.rechargeBalance("johnny@gmail.com", recharge);
    }

    @Test
    @Tag("PayMyBuddy")
    @DisplayName("Given paymentTransaction, when payMyBuddy, then payment is done correctly")
    public void givenPaymentTransaction_whenPayMyBuddy_thenPaymentIsDoneCorrectly() {
        PaymentTransactionDTO payment = new PaymentTransactionDTO("tom@gmail.com", "food",
                BigDecimal.valueOf(15.55));
        TransactionDTO transaction = new TransactionDTO(TransactionTypes.PAYMENT, "tom@gmail.com",
                LocalDate.now(), "food", BigDecimal.valueOf(15.55), BigDecimal.valueOf(0.08));

        TransactionDTO paymentSaved = transactionService.payMyBuddy("brad@gmail.com", payment);
        User brad = userService.getUserByEmail("brad@gmail.com");
        User tom = userService.getUserByEmail("tom@gmail.com");

        assertThat(paymentSaved).isEqualToComparingFieldByField(transaction);

        //balance of sender after payment = 200 - (15.55 + 0.08) = 184.37
        assertThat(brad.getBuddyAccount().getBalance()).isEqualTo(BigDecimal.valueOf(184.37));
        //balance  of receiver after payment = 800 + 15.55 = 815.55
        assertThat(tom.getBuddyAccount().getBalance()).isEqualTo(BigDecimal.valueOf(815.55));
    }

    @Test(expected = ResourceNotFoundException.class)
    @Tag("PayMyBuddy")
    @DisplayName("If buddy to pay is not in owner contact, when payMyBuddy, then throw ResourceNotFoundException")
    public void givenUnFoundBuddyInContact_whenPayMyBuddy_thenResourceNotFoundExceptionIsThrown() {
        PaymentTransactionDTO payment = new PaymentTransactionDTO("johnny@gmail.com", "food",
                BigDecimal.valueOf(30));

        transactionService.payMyBuddy("brad@gmail.com", payment);
    }

    @Test(expected = BadRequestException.class)
    @Tag("PayMyBuddy")
    @DisplayName("If funds is insufficient for payment, when payMyBuddy, then throw BadRequestException")
    public void givenInsufficientFunds_whenPayMyBuddy_thenBadRequestExceptionIsThrown() {
        PaymentTransactionDTO payment = new PaymentTransactionDTO("tom@gmail.com", "hotel",
                BigDecimal.valueOf(199.7));
        transactionService.payMyBuddy("brad@gmail.com", payment);
    }
}
