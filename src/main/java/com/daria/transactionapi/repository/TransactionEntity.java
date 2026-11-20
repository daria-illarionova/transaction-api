package com.daria.transactionapi.repository;

import com.daria.transactionapi.model.TransactionType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

// @Entity tells JPA “Treat this Java class as a persistent database object.”
// @Table(name = "transactions") tells JPA which database table corresponds to the entity.
@Entity
@Table(name = "transactions")
public class TransactionEntity {

    // @Id identifies the database primary key.
    // @GeneratedValue(strategy = GenerationType.IDENTITY) tells JPA that the database generates the ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false, length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private Instant createdAt;

    protected TransactionEntity() {
    }

    public TransactionEntity(
            Long id,
            BigDecimal amount,
            String currency,
            String description,
            TransactionType type,
            Instant createdAt) {

        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.type = type;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public TransactionType getType() {
        return type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}