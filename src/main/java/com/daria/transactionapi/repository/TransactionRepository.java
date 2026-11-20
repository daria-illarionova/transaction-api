package com.daria.transactionapi.repository;

import com.daria.transactionapi.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    Transaction save(Transaction transaction);

    List<Transaction> findAll();

    // GET findById()
    // Representing 2 possible outcomes: Optional<Transaction> containing Transaction OR Optional.empty()
    Optional<Transaction> findById(Long id);
}