package com.daria.transactionapi.dto;

import com.daria.transactionapi.model.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public class TransactionResponse {

    private Long id;
    private BigDecimal amount;
    private String currency;
    private String description;
    private TransactionType type;
    private Instant createdAt;

    public TransactionResponse() {
    }

    public TransactionResponse(
            Long id,
            BigDecimal amount,
            String currency,
            String description,
            TransactionType type,
            Instant createdAt
    ) {
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

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}