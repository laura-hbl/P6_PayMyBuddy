package com.paymybuddy.paymybuddy.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.paymybuddy.controller.BankAccountController;
import com.paymybuddy.paymybuddy.dto.BankAccountDTO;
import com.paymybuddy.paymybuddy.security.MyUserDetailsService;
import com.paymybuddy.paymybuddy.service.BankAccountService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BankAccountController.class)
public class BankAccountControllerTest {

    @MockBean
    private BankAccountService bankAccountService;

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

    private BankAccountDTO bankAccountDTO;

    @BeforeEach
    public void setUp() {
        bankAccountDTO = new BankAccountDTO("099NJK764FE8086", "09878KOLJ");

        when(loginEmailRetriever.getUsername(any(MockHttpServletRequest.class))).thenReturn("bradPitt@gmail.com");

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @Tag("AddBankAccount")
    @DisplayName("Given a bank account to add, when addBankAccount, then return Created status")
    public void givenABankAccountToAdd_whenAddBankAccount_thenReturnCreatedStatus() throws Exception {
        when(bankAccountService.createBankAccount(anyString(), any(BankAccountDTO.class))).thenReturn(bankAccountDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/bankAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bankAccountDTO)))
                .andExpect(status().isCreated());

        verify(bankAccountService).createBankAccount(anyString(), any(BankAccountDTO.class));
    }

    @Test
    @Tag("AddBankAccount")
    @DisplayName("If IBAN field is empty, when addBankAccount, then return BadRequest status and error message")
    public void givenEmptyIBAN_whenAddBankAccount_thenReturnBadRequestStatus() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/bankAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new BankAccountDTO("", "09878KOLJ"))))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        Assertions.assertThat(content).contains("IBAN code is required");
        verify(bankAccountService, times(0)).createBankAccount(anyString(),
                any(BankAccountDTO.class));
    }

    @Test
    @Tag("AddBankAccount")
    @DisplayName("If IBAN is not at least 14 characters, when addBankAccount, then return BadRequest status and error message")
    public void givenInvalidIBANCode_whenAddBankAccount_thenReturnBadRequestStatus() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/bankAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new BankAccountDTO("099N86H6", "09878KOLJ"))))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        Assertions.assertThat(content).contains("Please enter a valid IBAN code");
        verify(bankAccountService, times(0)).createBankAccount(anyString(),
                any(BankAccountDTO.class));
    }

    @Test
    @Tag("addBankAccount")
    @DisplayName("If BIC is more than 11 characters, when addBankAccount, when addBankAccount, then return BadRequest " +
            "status and error message")
    public void givenInvalidBicCode_whenAddBankAccount_thenReturnBadRequestStatus() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/bankAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new BankAccountDTO("099NJK764FE8086", "09876GFE4FCX65F"))))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        Assertions.assertThat(content).contains("Please enter a valid BIC code");
        verify(bankAccountService, times(0)).createBankAccount(anyString(),
                any(BankAccountDTO.class));
    }
}
