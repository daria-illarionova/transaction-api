package com.daria.transactionapi.controller;

import com.daria.transactionapi.dto.TransactionResponse;
import com.daria.transactionapi.model.Transaction;
import com.daria.transactionapi.service.TransactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daria.transactionapi.dto.CreateTransactionRequest;

// import com.daria.transactionapi.dto.TransactionRequest;
// Adding the POST endpoint: teaching Controller to accept this DTO
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

// Adding Request Validation
import jakarta.validation.Valid;

// import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    
    // Adding the GET endpoint
    @GetMapping
    public List<TransactionResponse> getTransactions() {

        List<Transaction> transactions = transactionService.getTransactions();

        return transactions.stream()
                .map(transaction -> new TransactionResponse(
                        transaction.getId(),
                        transaction.getAmount(),
                        transaction.getCurrency(),
                        transaction.getDescription(),
                        transaction.getType(),
                        transaction.getCreatedAt()
                ))
                .toList();
    }

    @GetMapping("/{id}")
    public TransactionResponse getTransaction(@PathVariable Long id) {

        Transaction transaction = transactionService.findById(id);

        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getDescription(),
                transaction.getType(),
                transaction.getCreatedAt()
        );
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(
                @Valid @RequestBody CreateTransactionRequest request) {

        Transaction transaction = new Transaction(
                null,
                request.getAmount(),
                request.getCurrency(),
                request.getDescription(),
                request.getType(),
                Instant.now()
        );

        // The service creates the transaction once.
        Transaction createdTransaction = transactionService.createTransaction(transaction);

        // Convert the domain object into the API response DTO.
        return new TransactionResponse(
                createdTransaction.getId(),
                createdTransaction.getAmount(),
                createdTransaction.getCurrency(),
                createdTransaction.getDescription(),
                createdTransaction.getType(),
                createdTransaction.getCreatedAt()
        );
    }

    /*
    - Adding the POST endpoint
    - Updated to include Request Validation: Added @Valid
    - Updated body: Responsible for converting TransactionRequest to Transaction before calling the Service.
                    Responsible for converting TransactionResponse.
    */
}