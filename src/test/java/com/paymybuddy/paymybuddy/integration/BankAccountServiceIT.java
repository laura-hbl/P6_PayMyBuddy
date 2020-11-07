package com.paymybuddy.paymybuddy.integration;

import com.paymybuddy.paymybuddy.dto.BankAccountDTO;
import com.paymybuddy.paymybuddy.exception.DataAlreadyRegisteredException;
import com.paymybuddy.paymybuddy.service.BankAccountService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource({"/application-test.properties"})
@Sql(scripts = "/schema-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BankAccountServiceIT {

    @Autowired
    private BankAccountService bankAccountService;

    @Test
    @Tag("CreateBankAccount")
    @DisplayName("Given a bank account, when createBankAccount, then bank account should be saved correctly")
    public void givenABankAccount_whenCreateBankAccount_thenBankAccountShouldBeSavedCorrectly() {
        BankAccountDTO bankAccount = new BankAccountDTO("FR50 5000 5000 5000 5000 5000 J05", "CRL8G54");
        BankAccountDTO bankAccountSaved = bankAccountService.createBankAccount("will@gmail.com",
                bankAccount);

        assertNotNull(bankAccountSaved);
        assertThat(bankAccountSaved).isEqualToComparingFieldByField(bankAccount);

    }

    @Test(expected = DataAlreadyRegisteredException.class)
    @Tag("CreateBankAccount")
    @DisplayName("If bank account is already registered, when createBankAccount, then throw DataAlreadyRegisteredException")
    public void givenAnRegisteredBankAccount_whenCreateBankAccount_thenDataAlreadyRegisteredExceptionIsThrown() {
        BankAccountDTO bankAccount = new BankAccountDTO("FR10 1000 1000 1000 1000 1000 J01", "CRLYGTED");
        bankAccountService.createBankAccount("brad@gmail.com", bankAccount);

    }
}
