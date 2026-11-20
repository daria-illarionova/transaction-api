/*
Created the persistence adapter

This class is the bridge between your domain model and your persistence model.

When the Service gives it: Transaction
It converts that into: TransactionEntity, 
Then jpaTransactionRepository.save(entity) causes JPA/Hibernate to persist the entity into PostgreSQL
After PostgreSQL generates the ID, the saved entity comes back with something like: id = 1
Then we convert it back into: Transaction, for the rest of your application.
*/

package com.daria.transactionapi.repository;

import com.daria.transactionapi.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final JpaTransactionRepository jpaTransactionRepository;

    public TransactionRepositoryImpl(JpaTransactionRepository jpaTransactionRepository) {
        this.jpaTransactionRepository = jpaTransactionRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {

        TransactionEntity entity = new TransactionEntity(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getDescription(),
                transaction.getType(),
                transaction.getCreatedAt()
        );

        TransactionEntity savedEntity = jpaTransactionRepository.save(entity);

        return new Transaction(
                savedEntity.getId(),
                savedEntity.getAmount(),
                savedEntity.getCurrency(),
                savedEntity.getDescription(),
                savedEntity.getType(),
                savedEntity.getCreatedAt()
        );
    }

    @Override
    public List<Transaction> findAll() {

        return jpaTransactionRepository.findAll()
                .stream()
                .map(entity -> new Transaction(
                        entity.getId(),
                        entity.getAmount(),
                        entity.getCurrency(),
                        entity.getDescription(),
                        entity.getType(),
                        entity.getCreatedAt()
                ))
                .toList();
    }

    // Implement findById() in the JPA adapter
    @Override
    public Optional<Transaction> findById(Long id) {

        return jpaTransactionRepository.findById(id)
                .map(entity -> new Transaction(
                        entity.getId(),
                        entity.getAmount(),
                        entity.getCurrency(),
                        entity.getDescription(),
                        entity.getType(),
                        entity.getCreatedAt()
                ));
    }
}