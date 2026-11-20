/*
 * Controller/API test
 *
 * @WebMvcTest loads the Spring MVC layer without loading the entire application.
 * MockMvc lets us send HTTP requests to the controller without starting a real server.
 */

package com.daria.transactionapi.controller;

import com.daria.transactionapi.service.TransactionService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.daria.transactionapi.controller.TransactionController;
// Imports to allow JSON inspection
import com.daria.transactionapi.model.Transaction;
import com.daria.transactionapi.model.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

// Imports to test POST
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import org.springframework.web.bind.annotation.ResponseStatus;
// import org.springframework.http.HttpStatus;

// Imports to test invalid transaction
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@WebMvcTest(
    controllers = TransactionController.class,
    properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.security.oauth2.server.resource.autoconfigure.web.OAuth2ResourceServerWebSecurityAutoConfiguration"
    }
)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @Test
    void shouldReturnTransactions() throws Exception {
        Transaction transaction = new Transaction(
                1L,
                new BigDecimal("49.99"),
                "CAD",
                "Grocery purchase",
                TransactionType.DEBIT,
                Instant.now()
        );

        when(transactionService.getTransactions())
                .thenReturn(List.of(transaction));

        mockMvc.perform(get("/api/v1/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].amount").value(49.99))
                .andExpect(jsonPath("$[0].currency").value("CAD"))
                .andExpect(jsonPath("$[0].description").value("Grocery purchase"))
                .andExpect(jsonPath("$[0].type").value("DEBIT"));
    }
    @Test
    void shouldReturnEmptyListWhenThereAreNoTransactions() throws Exception {
        when(transactionService.getTransactions())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldCreateTransaction() throws Exception {
        Transaction transaction = new Transaction(
                1L,
                new BigDecimal("49.99"),
                "CAD",
                "Grocery purchase",
                TransactionType.DEBIT,
                Instant.now()
        );

        when(transactionService.createTransaction(org.mockito.ArgumentMatchers.any(Transaction.class)))
                .thenReturn(transaction);

        String requestBody = """
                {
                    "amount": 49.99,
                    "currency": "CAD",
                    "description": "Grocery purchase",
                    "type": "DEBIT"
                }
                """;

        mockMvc.perform(
                post("/api/v1/transactions")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(49.99))
                .andExpect(jsonPath("$.currency").value("CAD"))
                .andExpect(jsonPath("$.description").value("Grocery purchase"))
                .andExpect(jsonPath("$.type").value("DEBIT"));
    }

    /*
    Testing that 1) API rejects invalid input (valid JSON + invalid values) & 
                2) Invalid input doesn't reach the service layer
    */
    @Test
    void shouldRejectInvalidTransaction() throws Exception {
        String requestBody = """
                {
                    "amount": -10,
                    "currency": "CAD",
                    "description": "Invalid transaction",
                    "type": "DEBIT"
                }
                """;

        mockMvc.perform(
                post("/api/v1/transactions")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isBadRequest());
        
        verify(transactionService, never())
                .createTransaction(org.mockito.ArgumentMatchers.any(Transaction.class));
    }

    /*
    Testing malformed/invalid JSON
    */
    @Test
    void shouldRejectMalformedJson() throws Exception {
        String requestBody = """
                {
                    "amount": 49.99,
                    "currency": "CAD",
                """;

        mockMvc.perform(
                post("/api/v1/transactions")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isBadRequest());
    }
}