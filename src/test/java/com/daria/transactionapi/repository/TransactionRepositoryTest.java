package com.daria.transactionapi.repository;

import com.daria.transactionapi.model.Transaction;
import com.daria.transactionapi.model.TransactionType;
import com.daria.transactionapi.repository.TransactionRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* 
Using Spring Boot because we want Spring to create the real application infrastructure.
Not using Mockito here (MockitoBean, Mock) because mocking the persistence layer would defeat the purpose of this test.
*/
@SpringBootTest
class TransactionRepositoryTest {

    // Autowired the actual TransactionRepository not TransactionRepositoryImpl
    // Application is designed so that the service depends on: TransactionRepository
    // When spring sees TransactionRepository, it injects the implementation 
    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void shouldSaveAndRetrieveTransaction() {

        Transaction transaction = new Transaction(
                null,
                new BigDecimal("49.99"),
                "CAD",
                "Repository test transaction",
                TransactionType.DEBIT,
                Instant.now()
        );

        Transaction savedTransaction =
                transactionRepository.save(transaction);
                // Transaction -> TransactionEntity inside TransactionRepositoryImpl

        assertNotNull(savedTransaction.getId());
        assertEquals(transaction.getAmount(), savedTransaction.getAmount());
        assertEquals(transaction.getCurrency(), savedTransaction.getCurrency());
        assertEquals(transaction.getDescription(), savedTransaction.getDescription());
        assertEquals(transaction.getType(), savedTransaction.getType());
        assertEquals(transaction.getCreatedAt(), savedTransaction.getCreatedAt());

        /* findById()
        Repository testing: findById()
        Verifying the outcomes for an existing/nonexistent transaction
            2 outcomes: 
            - ID exists -> Optional<Transaction>
            - ID doesn't exist -> Optional.empty()
        */
        Optional<Transaction> foundTransaction =
                transactionRepository.findById(savedTransaction.getId());

        assertTrue(foundTransaction.isPresent());

        Transaction retrievedTransaction =
                foundTransaction.get();

        assertEquals(savedTransaction.getId(), retrievedTransaction.getId());
        assertEquals(savedTransaction.getAmount(), retrievedTransaction.getAmount());
        assertEquals(savedTransaction.getCurrency(), retrievedTransaction.getCurrency());
        assertEquals(savedTransaction.getDescription(), retrievedTransaction.getDescription());
        assertEquals(savedTransaction.getType(), retrievedTransaction.getType());
        assertEquals(savedTransaction.getCreatedAt(), retrievedTransaction.getCreatedAt());
    }

    @Test
    void shouldFindAllTransactions() {

        Transaction transaction = new Transaction(
                null,
                new BigDecimal("25.00"),
                "CAD",
                "Find all test transaction",
                TransactionType.CREDIT,
                Instant.now()
        );

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        /* findAll()
        Verifies that transactionRepository.findAll(); actually returns persisted domain objects.
        */
        List<Transaction> transactions =
                transactionRepository.findAll();

        assertNotNull(transactions);
        assertFalse(transactions.isEmpty());

        assertTrue(
                transactions.stream()
                        .anyMatch(t -> t.getId().equals(savedTransaction.getId()))
        );
    }

    @Test
    void shouldReturnEmptyWhenTransactionDoesNotExist() {

        Optional<Transaction> result =
                transactionRepository.findById(999999L);

        assertTrue(result.isEmpty());
    }
}