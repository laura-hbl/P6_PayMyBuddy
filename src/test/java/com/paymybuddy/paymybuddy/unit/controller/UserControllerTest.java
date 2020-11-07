package com.paymybuddy.paymybuddy.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.paymybuddy.controller.UserController;
import com.paymybuddy.paymybuddy.dto.ConnectionDTO;
import com.paymybuddy.paymybuddy.dto.ContactsDTO;
import com.paymybuddy.paymybuddy.dto.UserDTO;
import com.paymybuddy.paymybuddy.security.MyUserDetailsService;
import com.paymybuddy.paymybuddy.service.UserService;
import com.paymybuddy.paymybuddy.util.LoginEmailRetriever;
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

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

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

    private UserDTO userDTO;

    private ConnectionDTO connectionDTO;

    @BeforeEach
    public void setUp() {
        userDTO = new UserDTO("Brad", "Pitt", "bradPitt@gmail.com", "myPassword",
                "0601331013");

        connectionDTO = new ConnectionDTO("buddyEmail@gmail.com");

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @Tag("registerUser")
    @DisplayName("Given an user to register, when registerUser, then return Created status")
    public void givenAnUserToRegister_whenRegisterUserRequest_thenReturnCreatedStatus() throws Exception {
        when(userService.registerUser(any(UserDTO.class))).thenReturn(userDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        assertThat(content).contains("bradPitt@gmail.com");
        verify(userService).registerUser(any(UserDTO.class));
    }

    @Test
    @Tag("registerUser")
    @DisplayName("If field last name is empty, when registerUser, then return BadRequest status " +
            "and error message")
    public void givenAnEmptyLastNameField_whenRegisterUserRequest_thenReturnBadRequestStatusAndErrorMessage()
            throws Exception {
        userDTO = new UserDTO("Brad", "", "bradPitt@gmail.com", "myPassword",
                "0601331013");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        assertThat(content).contains("Last name is required");
        verify(userService, times(0)).registerUser(any(UserDTO.class));
    }

    @Test
    @Tag("registerUser")
    @DisplayName("If password is not at least 5 characters, when registerUser then return BadRequest status and" +
            " error message")
    public void givenAnInvalidPassword_whenRegisterUserRequest_thenReturnBadRequestStatusAndErrorMessage()
            throws Exception {
        userDTO = new UserDTO("Brad", "Pitt", "bradPitt@gmail.com", "pass",
                "0601331013");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        assertThat(content).contains("The Password you provided must have at least 5 characters");
        verify(userService, times(0)).registerUser(any(UserDTO.class));
    }

    @Test
    @Tag("addConnection")
    @DisplayName("Given a buddy to add, when addConnection, then return OK status")
    public void givenABuddyToAdd_whenAddConnection_thenReturnOkStatus() throws Exception {
        ContactsDTO contacts = new ContactsDTO();
        contacts.setContacts(new ArrayList<>(Arrays.asList("buddyEmail@gmail.com")));
        when(loginEmailRetriever.getUsername(any(MockHttpServletRequest.class))).thenReturn("bradPitt@gmail.com");
        when(userService.addConnection(anyString(), anyString())).thenReturn(contacts);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/contact")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(connectionDTO)))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        assertThat(content).contains("buddyEmail@gmail.com");
        verify(userService).addConnection(anyString(), anyString());
    }

    @Test
    @Tag("addConnection")
    @DisplayName("If field buddyEmail is empty, when addConnection, then return BadRequest status and error message")
    public void givenAnEmptyBuddyEmail_whenAddConnection_thenReturnBadRequestStatusAndErrorMessage() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/contact")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ConnectionDTO(""))))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        assertThat(content).contains("Buddy Email address is required");
        verify(userService, times(0)).addConnection(anyString(), anyString());
    }

    @Test
    @Tag("addConnection")
    @DisplayName("If buddyEmail is not at least 3 characters, when addConnection, then return BadRequest status" +
            " and error message")
    public void givenAnInvalidBuddyEmail_whenAddConnection_thenReturnBadRequestStatusAndErrorMessage() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/contact")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ConnectionDTO("a"))))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        assertThat(content).contains("Please enter a valid email address");
        verify(userService, times(0)).addConnection(anyString(), anyString());
    }
}
