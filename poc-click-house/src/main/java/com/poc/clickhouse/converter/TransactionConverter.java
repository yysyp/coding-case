package com.poc.clickhouse.converter;


import com.poc.clickhouse.dto.TransactionResponse;
import com.poc.clickhouse.entity.Transaction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Converter utility for mapping between Transaction entity and DTO objects.
 * Handles conversion in both directions while maintaining audit fields.
 */
@Component
public class TransactionConverter {

    /**
     * Converts a Transaction entity to TransactionResponse DTO.
     *
     * @param transaction The entity to convert
     * @return The converted DTO
     */
    public TransactionResponse toResponse(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getTransactionType(),
                transaction.getAccountNumber(),
                transaction.getCreatedAt(),
                transaction.getCreatedBy(),
                transaction.getUpdatedAt(),
                transaction.getUpdatedBy()
        );
    }

    /**
     * Converts a Transaction entity to TransactionResponse DTO with custom timestamps.
     *
     * @param transaction The entity to convert
     * @param createdAt   Custom creation timestamp
     * @param createdBy   User who created the record
     * @param updatedAt   Custom update timestamp
     * @param updatedBy   User who updated the record
     * @return The converted DTO
     */
    public TransactionResponse toResponseWithAudit(Transaction transaction, 
                                                  LocalDateTime createdAt, String createdBy,
                                                  LocalDateTime updatedAt, String updatedBy) {
        if (transaction == null) {
            return null;
        }

        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getTransactionType(),
                transaction.getAccountNumber(),
                createdAt,
                createdBy,
                updatedAt,
                updatedBy
        );
    }
}
