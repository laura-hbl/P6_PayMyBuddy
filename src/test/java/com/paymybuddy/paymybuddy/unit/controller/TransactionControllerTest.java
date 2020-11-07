package com.paymybuddy.paymybuddy.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.paymybuddy.constants.TransactionTypes;
import com.paymybuddy.paymybuddy.controller.TransactionController;
import com.paymybuddy.paymybuddy.dto.PaymentTransactionDTO;
import com.paymybuddy.paymybuddy.dto.PersonalTransactionDTO;
import com.paymybuddy.paymybuddy.dto.TransactionDTO;
import com.paymybuddy.paymybuddy.security.MyUserDetailsService;
import com.paymybuddy.paymybuddy.service.TransactionService;
import com.paymybuddy.paymybuddy.util.LoginEmailRetriever;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private LoginEmailRetriever loginEmailRetriever;

    @MockBean
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        when(loginEmailRetriever.getUsername(any(MockHttpServletRequest.class))).thenReturn("bradPitt@gmail.com");

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @Tag("transferToBankAccount")
    @DisplayName("Given PersonalTransaction, when transferToBankAccount, then return OK status")
    public void givenAPersonalTransaction_whenTransferToBankAccount_thenReturnOKStatus() throws Exception {
        PersonalTransactionDTO transfer = new PersonalTransactionDTO("transfer", BigDecimal.valueOf(100));
        TransactionDTO transaction = new TransactionDTO(TransactionTypes.TRANSFER, "bradPitt@gmail.com", LocalDate.now(),
                "transfer", BigDecimal.valueOf(100),  BigDecimal.valueOf(0.5));
        when(transactionService.transferToBankAccount(anyString(), any(PersonalTransactionDTO.class))).
                thenReturn(transaction);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transfer)))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        Assertions.assertThat(content).contains("transfer");
        verify(transactionService).transferToBankAccount(anyString(), any(PersonalTransactionDTO.class));
    }


    @Test
    @Tag("transferToBankAccount")
    @DisplayName("If amount is 0, when transferToBankAccount, then return BadRequest status and error message")
    public void givenAmountOfZero_whenTransferToBankAccount_thenReturnBadRequestStatus() throws Exception {
        PersonalTransactionDTO transfer = new PersonalTransactionDTO("transfer", BigDecimal.valueOf(0));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transfer)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        Assertions.assertThat(content).contains("minimum amount authorized is 1");
        verify(transactionService, times(0)).transferToBankAccount(anyString(),
                any(PersonalTransactionDTO.class));
    }

    @Test
    @Tag("transferToBankAccount")
    @DisplayName("If amount is more than maximum authorized, when transferToBankAccount, then return BadRequest " +
            "status and error message")
    public void givenInvalidAmount_whenTransferToBankAccount_thenReturnBadRequestStatus() throws Exception {
        PersonalTransactionDTO transfer = new PersonalTransactionDTO("transfer", BigDecimal.valueOf(1000));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transfer)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        Assertions.assertThat(content).contains("maximum amount authorized is 999.99");
        verify(transactionService, times(0)).transferToBankAccount(anyString(),
                any(PersonalTransactionDTO.class));
    }

    @Test
    @Tag("transferToBankAccount")
    @DisplayName("If amount is null, when transferToBankAccount, then return BadRequest status and error message")
    public void givenNullAmount_whenTransferToBankAccount_thenReturnBadRequestStatus() throws Exception {
        PersonalTransactionDTO transfer = new PersonalTransactionDTO("transfer", null);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transfer)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        Assertions.assertThat(content).contains("Amount is required");
        verify(transactionService, times(0)).transferToBankAccount(anyString(),
                any(PersonalTransactionDTO.class));
    }

    @Test
    @Tag("rechargeBalance")
    @DisplayName("Given PersonalTransaction, when rechargeBalance, then return OK status")
    public void givenPersonalTransaction_whenRechargeBalance_thenReturnOKStatus() throws Exception {
        PersonalTransactionDTO recharge = new PersonalTransactionDTO("recharge", BigDecimal.valueOf(100));
        TransactionDTO transaction = new TransactionDTO(TransactionTypes.RECHARGE, "bradPitt@gmail.com",
                LocalDate.now(), "recharge", BigDecimal.valueOf(100),  BigDecimal.valueOf(0.5));
        when(transactionService.rechargeBalance(anyString(), any(PersonalTransactionDTO.class))).thenReturn(transaction);

        mockMvc.perform(MockMvcRequestBuilders.post("/recharge")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recharge)))
                .andExpect(status().isOk());

        verify(transactionService).rechargeBalance(anyString(), any(PersonalTransactionDTO.class));
    }

    @Test
    @Tag("rechargeBalance")
    @DisplayName("If description field is empty, when rechargeBalance, then return return BadRequest status " +
            "and error message")
    public void givenEmptyDescriptionField_whenRechargeBalance_thenReturnBadRequestStatus() throws Exception {
        PersonalTransactionDTO recharge = new PersonalTransactionDTO("", BigDecimal.valueOf(100));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/recharge")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recharge)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        Assertions.assertThat(content).contains("Description is required");
        verify(transactionService, times(0)).rechargeBalance(anyString(),
                any(PersonalTransactionDTO.class));
    }

    @Test
    @Tag("rechargeBalance")
    @DisplayName("if amount have more than 2 decimal, when rechargeBalance, then return BadRequest status")
    public void givenAmountWithThreeDecimal_whenRechargeBalance_thenReturnBadRequestStatus() throws Exception {
        PersonalTransactionDTO recharge = new PersonalTransactionDTO("recharge", BigDecimal.valueOf(100.987));

       mockMvc.perform(MockMvcRequestBuilders.post("/recharge")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recharge)))
                .andExpect(status().isBadRequest());

        verify(transactionService, times(0)).rechargeBalance(anyString(),
                any(PersonalTransactionDTO.class));
    }

    @Test
    @Tag("rechargeBalance")
    @DisplayName("Given null amount, when rechargeBalance, then return BadRequest status and error message")
    public void givenNullAmount_whenRechargeBalance_thenReturnBadRequestStatus() throws Exception {
        PersonalTransactionDTO recharge = new PersonalTransactionDTO("recharge", null);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/recharge")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recharge)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        Assertions.assertThat(content).contains("Amount is required");
        verify(transactionService, times(0)).rechargeBalance(anyString(),
                any(PersonalTransactionDTO.class));
    }

    @Test
    @Tag("payment")
    @DisplayName("Given PaymentTransaction, when payment, then return OK status")
    public void givenPaymentTransaction_whenPayment_thenReturnOKStatus() throws Exception {
        PaymentTransactionDTO payment = new PaymentTransactionDTO("buddy@gmail.com", "beers",
                BigDecimal.valueOf(10));
        TransactionDTO transaction = new TransactionDTO(TransactionTypes.PAYMENT, "buddy@gmail.com",
                LocalDate.now(), "beers", BigDecimal.valueOf(10), BigDecimal.valueOf(0.05));
        when(transactionService.payMyBuddy(anyString(), any(PaymentTransactionDTO.class))).thenReturn(transaction);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payment)))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        Assertions.assertThat(content).contains("beers");
        verify(transactionService).payMyBuddy(anyString(), any(PaymentTransactionDTO.class));
    }

    @Test
    @Tag("payment")
    @DisplayName("Given empty fields, when payment, then return BadRequest status and error messages")
    public void givenEmptyFields_whenPayment_thenReturnBadRequestStatus() throws Exception {
        PaymentTransactionDTO payment = new PaymentTransactionDTO("", "", BigDecimal.valueOf(10));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payment)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        Assertions.assertThat(content).contains("Buddy email is required", "Description is required");
        verify(transactionService, times(0)).payMyBuddy(anyString(), any(PaymentTransactionDTO.class));
    }

    @Test
    @Tag("payment")
    @DisplayName("Given negative amount, when payment, then return BadRequest status and error message")
    public void givenNegativeAmount_whenPayment_thenReturnBadRequestStatus() throws Exception {
        PaymentTransactionDTO payment = new PaymentTransactionDTO("buddyEmail@gmail.com", "beers",
                BigDecimal.valueOf(-10));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payment)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        Assertions.assertThat(content).contains("minimum amount authorized is 1");
        verify(transactionService, times(0)).payMyBuddy(anyString(), any(PaymentTransactionDTO.class));
    }

    @Test
    @Tag("payment")
    @DisplayName("Given null amount, when payment, then return BadRequest status and error message")
    public void givenNullAmount_whenPayment_thenReturnBadRequestStatus() throws Exception {
        PaymentTransactionDTO payment = new PaymentTransactionDTO("buddyEmail@gmail.com", "beers", null);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payment)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        Assertions.assertThat(content).contains("Amount is required");
        verify(transactionService, times(0)).payMyBuddy(anyString(), any(PaymentTransactionDTO.class));
    }
}
