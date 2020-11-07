package com.paymybuddy.paymybuddy.integration;

import com.paymybuddy.paymybuddy.model.BuddyAccount;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.service.BuddyAccountService;
import com.paymybuddy.paymybuddy.service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource({"/application-test.properties"})
@Sql(scripts = "/schema-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BuddyAccountServiceIT {

    @Autowired
    private BuddyAccountService buddyAccountService;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("Given a buddy account, when saveBuddyAccount, then buddy account should be saved correctly")
    public void givenABuddyAccount_whenSaveBuddyAccount_thenBuddyAccountShouldBeSavedCorrectly() {
        User will = userService.getUserByEmail("will@gmail.com");
        BuddyAccount buddyAccount = new BuddyAccount(will, BigDecimal.ZERO);

        BuddyAccount buddyAccountSaved = buddyAccountService.saveBuddyAccount(buddyAccount);

        assertNotNull(buddyAccountSaved);
        assertThat(buddyAccountSaved.getBalance()).isEqualTo(BigDecimal.ZERO);
    }
}
