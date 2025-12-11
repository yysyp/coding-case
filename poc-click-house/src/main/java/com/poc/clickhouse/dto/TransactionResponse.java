package com.poc.clickhouse.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Response DTO for transaction queries.
 */
@Schema(description = "Response object for transaction queries")
public class TransactionResponse {
    
    @Schema(description = "Unique identifier of the transaction", example = "1")
    private Long id;
    
    @Schema(description = "Transaction amount", example = "100.50")
    private Double amount;
    
    @Schema(description = "Currency code", example = "USD")
    private String currency;
    
    @Schema(description = "Transaction type", example = "CREDIT")
    private String transactionType;
    
    @Schema(description = "Account number", example = "ACC123456")
    private String accountNumber;
    
    @Schema(description = "Timestamp when the record was created")
    private LocalDateTime createdAt;
    
    @Schema(description = "User who created the record")
    private String createdBy;
    
    @Schema(description = "Timestamp when the record was last updated")
    private LocalDateTime updatedAt;
    
    @Schema(description = "User who last updated the record")
    private String updatedBy;

    // Constructors
    public TransactionResponse() {}

    public TransactionResponse(Long id, Double amount, String currency, String transactionType,
                              String accountNumber, LocalDateTime createdAt, String createdBy,
                              LocalDateTime updatedAt, String updatedBy) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.transactionType = transactionType;
        this.accountNumber = accountNumber;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
