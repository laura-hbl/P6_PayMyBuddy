package com.paymybuddy.paymybuddy.unit.service;

import com.paymybuddy.paymybuddy.model.BuddyAccount;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.BuddyAccountRepository;
import com.paymybuddy.paymybuddy.service.BuddyAccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuddyAccountServiceTest {

    @Mock
    private BuddyAccountRepository buddyAccountRepository;

    @InjectMocks
    private BuddyAccountService buddyAccountService;

    private static User owner;

    private static BuddyAccount buddyAccount;

    @Before
    public void setUp() {
        owner = new User("Laura", "Habdul", "laurahbl@gmail.com", "HjuIY9jk5op&tc",
                "0601331013");
        buddyAccount = new BuddyAccount(owner, BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Given a buddy account, when saveBuddyAccount, then buddy account should be saved correctly")
    public void givenABuddyAccount_whenSaveBuddyAccount_thenBuddyAccountShouldBeSavedCorrectly() {
        when(buddyAccountRepository.save(any(BuddyAccount.class))).thenReturn(buddyAccount);

        BuddyAccount buddyAccountSaved = buddyAccountService.saveBuddyAccount(buddyAccount);

        assertThat(buddyAccountSaved).isEqualToComparingFieldByField(buddyAccount);
        verify(buddyAccountRepository).save(any(BuddyAccount.class));
    }
}
