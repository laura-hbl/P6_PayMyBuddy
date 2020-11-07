package com.paymybuddy.paymybuddy.unit.service;

import com.paymybuddy.paymybuddy.dto.BankAccountDTO;
import com.paymybuddy.paymybuddy.exception.DataAlreadyRegisteredException;
import com.paymybuddy.paymybuddy.model.BankAccount;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.BankAccountRepository;
import com.paymybuddy.paymybuddy.service.BankAccountService;
import com.paymybuddy.paymybuddy.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private BankAccountService bankAccountService;

    private static User owner;

    private static BankAccountDTO bankAccountDTO;

    private static BankAccount bankAccount;

    @Before
    public void setUp() {
        owner = new User("Laura", "Habdul", "laurahbl@gmail.com", "HjuIY9jk5op&tc",
                "0601331013");
        bankAccountDTO = new BankAccountDTO("099NJK65FDE9M975K", "09878KO0J");
        bankAccount = new BankAccount(owner, "099NJK65FDE9M975K", "09878KO0J");
    }

    @Test
    @Tag("CreateBankAccount")
    @DisplayName("Given a bank account, when createBankAccount, then bank account should be saved correctly")
    public void givenABankAccount_whenCreateBankAccount_thenBankAccountShouldBeSavedCorrectly() {
        when(userService.getUserByEmail(anyString())).thenReturn(owner);
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(bankAccount);

        BankAccountDTO bankAccountSaved = bankAccountService.createBankAccount("laurahbl@gmail.com",
                bankAccountDTO);

        assertThat(bankAccountSaved).isEqualToComparingFieldByField(bankAccountDTO);
        InOrder inOrder = inOrder(userService, bankAccountRepository);
        inOrder.verify(userService).getUserByEmail(anyString());
        inOrder.verify(bankAccountRepository).save(any(BankAccount.class));
    }

    @Test(expected = DataAlreadyRegisteredException.class)
    @Tag("CreateBankAccount")
    @DisplayName("If bank account is already registered, when createBankAccount, then throw DataAlreadyRegisteredException")
    public void givenAnRegisteredBankAccount_whenCreateBankAccount_thenDataAlreadyRegisteredExceptionIsThrown() {
        owner.setBankAccount(bankAccount);
        when(userService.getUserByEmail(anyString())).thenReturn(owner);

        bankAccountService.createBankAccount("laurahbl@gmail.com", bankAccountDTO);

        verify(userService).getUserByEmail(anyString());
    }
}
