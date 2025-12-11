package com.poc.clickhouse.controller;


import com.poc.clickhouse.converter.TransactionConverter;
import com.poc.clickhouse.dto.TransactionRequest;
import com.poc.clickhouse.dto.TransactionResponse;
import com.poc.clickhouse.entity.Transaction;
import com.poc.clickhouse.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing financial transactions.
 * Provides endpoints for CRUD operations on transactions stored in ClickHouse.
 */
@RestController
@RequestMapping("/api/v1/transactions")
@Tag(name = "Transaction Management", description = "APIs for managing financial transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionConverter transactionConverter;

    @Autowired
    public TransactionController(TransactionService transactionService, TransactionConverter transactionConverter) {
        this.transactionService = transactionService;
        this.transactionConverter = transactionConverter;
    }

    /**
     * Creates a new transaction.
     *
     * @param request The transaction details
     * @return The created transaction
     */
    @Operation(summary = "Create a new transaction", description = "Creates a new financial transaction record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction created successfully",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @Parameter(description = "Transaction details") 
            @Valid @RequestBody TransactionRequest request) {
        
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setTransactionType(request.getTransactionType());
        transaction.setAccountNumber(request.getAccountNumber());
        
        Transaction savedTransaction = transactionService.createTransaction(transaction, "api-user");
        TransactionResponse response = transactionConverter.toResponse(savedTransaction);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Retrieves all transactions.
     *
     * @return List of all transactions
     */
    @Operation(summary = "Get all transactions", description = "Retrieves all financial transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved transactions",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        List<TransactionResponse> responses = transactions.stream()
                .map(transactionConverter::toResponse)
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    /**
     * Retrieves a transaction by ID.
     *
     * @param id The transaction ID
     * @return The requested transaction
     */
    @Operation(summary = "Get transaction by ID", description = "Retrieves a specific financial transaction by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved transaction",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(
            @Parameter(description = "ID of the transaction to retrieve") 
            @PathVariable Long id) {
        
        return transactionService.getTransactionById(id)
                .map(transaction -> {
                    TransactionResponse response = transactionConverter.toResponse(transaction);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Retrieves transactions by account number.
     *
     * @param accountNumber The account number
     * @return List of transactions for the account
     */
    @Operation(summary = "Get transactions by account number", description = "Retrieves all transactions for a specific account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved transactions",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByAccountNumber(
            @Parameter(description = "Account number to search for") 
            @PathVariable String accountNumber) {
        
        List<Transaction> transactions = transactionService.getTransactionsByAccountNumber(accountNumber);
        List<TransactionResponse> responses = transactions.stream()
                .map(transactionConverter::toResponse)
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    /**
     * Updates an existing transaction.
     *
     * @param id      The transaction ID
     * @param request The updated transaction details
     * @return The updated transaction
     */
    @Operation(summary = "Update transaction", description = "Updates an existing financial transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction updated successfully",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @Parameter(description = "ID of the transaction to update") 
            @PathVariable Long id,
            @Parameter(description = "Updated transaction details") 
            @Valid @RequestBody TransactionRequest request) {
        
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setTransactionType(request.getTransactionType());
        transaction.setAccountNumber(request.getAccountNumber());
        
        return transactionService.updateTransaction(id, transaction, "api-user")
                .map(updatedTransaction -> {
                    TransactionResponse response = transactionConverter.toResponse(updatedTransaction);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Deletes a transaction by ID.
     *
     * @param id The transaction ID
     * @return Confirmation of deletion
     */
    @Operation(summary = "Delete transaction", description = "Deletes a financial transaction by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transaction deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @Parameter(description = "ID of the transaction to delete") 
            @PathVariable Long id) {
        
        boolean deleted = transactionService.deleteTransaction(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
