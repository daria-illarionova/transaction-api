package com.daria.transactionapi.service;

import com.daria.transactionapi.model.Transaction;
// import com.daria.transactionapi.model.TransactionType;
import org.springframework.stereotype.Service;
// Connecting the Service to the Repository
import com.daria.transactionapi.repository.TransactionRepository;
// GET findById()
import com.daria.transactionapi.exception.TransactionNotFoundException;

// import java.math.BigDecimal;
// import java.time.Instant;
import java.util.List;

@Service
public class TransactionService {

    /*
    Updating Service class to receive the repository through constructor injection
    */
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getTransactions() {

        // temporary in-memory data
        /*Transaction transaction = new Transaction(
                1L,
                new BigDecimal("49.99"),
                "CAD",
                "Grocery purchase",
                TransactionType.DEBIT,
                Instant.now()
        );
        return List.of(transaction);
        */
        return transactionRepository.findAll();
    }

    // createTransaction method accepts a Transaction
    public Transaction createTransaction(Transaction transaction) {

        // Business logic will eventually go here.

        // return transaction;
        return transactionRepository.save(transaction);
    }

    // Adding GET findById() to the Service
    public Transaction findById(Long id) {

        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }
}