// This puts the test in the same logical package as TransactionService
package com.daria.transactionapi.service;

import com.daria.transactionapi.exception.TransactionNotFoundException;
import com.daria.transactionapi.model.Transaction;
import com.daria.transactionapi.model.TransactionType;
import com.daria.transactionapi.repository.TransactionRepository;
import com.daria.transactionapi.service.TransactionService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void shouldReturnAllTransactions() {

        // Arrange
        Transaction transaction1 = new Transaction(
                1L,
                new BigDecimal("49.99"),
                "CAD",
                "Grocery purchase",
                TransactionType.DEBIT,
                Instant.now()
        );

        Transaction transaction2 = new Transaction(
                2L,
                new BigDecimal("100.00"),
                "CAD",
                "Salary",
                TransactionType.CREDIT,
                Instant.now()
        );

        List<Transaction> transactions = List.of(
                transaction1,
                transaction2
        );

        when(transactionRepository.findAll()).thenReturn(transactions);

        // Act
        List<Transaction> result = transactionService.getTransactions();

        // Assert
        assertEquals(2, result.size());
        assertEquals(transactions, result);

        verify(transactionRepository).findAll();
    }

    @Test
    void shouldCreateTransaction() {

        // Arrange
        Transaction transaction = new Transaction(
                null,
                new BigDecimal("49.99"),
                "CAD",
                "Grocery purchase",
                TransactionType.DEBIT,
                Instant.now()
        );

        Transaction savedTransaction = new Transaction(
                1L,
                new BigDecimal("49.99"),
                "CAD",
                "Grocery purchase",
                TransactionType.DEBIT,
                transaction.getCreatedAt()
        );

        when(transactionRepository.save(transaction)).thenReturn(savedTransaction);
            // Service sends: Transaction(id=null) -> Mock Repository -> Returns: Transaction(id=1) recall, the database normally generates the ID

        // Act
        Transaction result = transactionService.createTransaction(transaction);
            // calls the real service

        // Assert
        assertEquals(savedTransaction, result);
            // confirms that the service returned the repository's result

        verify(transactionRepository).save(transaction);
            // confirms that save() was actually called
    }

    @Test
    void shouldFindTransactionById() {

        // Arrange
        Long transactionId = 1L;

        Transaction transaction = new Transaction(
                transactionId,
                new BigDecimal("49.99"),
                "CAD",
                "Grocery purchase",
                TransactionType.DEBIT,
                Instant.now()
        );

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        // Act
        Transaction result = transactionService.findById(transactionId);

        // Assert
        assertEquals(transaction, result);

        /*
        Caution with equals()
        Compares object identity rather than the individual fields.

        equals()/hashCode()
        */

        verify(transactionRepository).findById(transactionId);
    }

    @Test
    void shouldThrowExceptionWhenTransactionDoesNotExist() {

        // Arrange
        Long transactionId = 999L;

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        // Act + Assert
        TransactionNotFoundException exception =
                assertThrows(
                        TransactionNotFoundException.class,
                        () -> transactionService.findById(transactionId)
                );

        assertTrue(exception.getMessage().contains("999"));

        verify(transactionRepository).findById(transactionId);
    }
}