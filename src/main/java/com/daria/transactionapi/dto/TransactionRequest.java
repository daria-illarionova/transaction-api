package com.daria.transactionapi.dto;

import com.daria.transactionapi.model.TransactionType;
// Adding Validation Rules
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class TransactionRequest {

    // Update variables to include Validation Rules

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be a 3-letter code")
    private String currency;

    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @NotNull(message = "Transaction type is required")
    private TransactionType type;

    /*
    Note: Using a No-Argument Constructor
    This is because Spring/Jackson needs to be able to create the Java object when it receives JSON.
    */
    public TransactionRequest() {
    }

    public TransactionRequest(
            BigDecimal amount,
            String currency,
            String description,
            TransactionType type
    ) {
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.type = type;
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
}