/*
Created the Spring Data JPA repository

Note: not the repository your Service will depend on.
It is the Spring Data JPA repository that knows how to communicate with the database.
*/

package com.daria.transactionapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTransactionRepository extends JpaRepository<TransactionEntity, Long> {
}