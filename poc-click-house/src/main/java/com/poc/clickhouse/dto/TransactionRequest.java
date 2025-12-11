package com.poc.clickhouse.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

/**
 * Request DTO for creating/updating transactions.
 */
@Schema(description = "Request object for transaction operations")
public class TransactionRequest {
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @Schema(description = "Transaction amount", example = "100.50")
    private Double amount;
    
    @NotBlank(message = "Currency is required")
    @Schema(description = "Currency code", example = "USD")
    private String currency;
    
    @NotBlank(message = "Transaction type is required")
    @Schema(description = "Transaction type", example = "CREDIT")
    private String transactionType;
    
    @NotBlank(message = "Account number is required")
    @Schema(description = "Account number", example = "ACC123456")
    private String accountNumber;

    // Constructors
    public TransactionRequest() {}

    public TransactionRequest(Double amount, String currency, String transactionType, String accountNumber) {
        this.amount = amount;
        this.currency = currency;
        this.transactionType = transactionType;
        this.accountNumber = accountNumber;
    }

    // Getters and Setters
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
