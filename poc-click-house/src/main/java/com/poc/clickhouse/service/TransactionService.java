package com.poc.clickhouse.service;


import com.poc.clickhouse.entity.Transaction;
import com.poc.clickhouse.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing financial transactions.
 * Handles business logic and coordination between repository and controller layers.
 */
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Initializes the database table.
     */
    public void initializeDatabase() {
        transactionRepository.initializeTable();
    }

    /**
     * Creates a new transaction.
     *
     * @param transaction The transaction to create
     * @param currentUser The user performing the operation
     * @return The created transaction
     */
    public Transaction createTransaction(Transaction transaction, String currentUser) {
        // Set audit fields
        LocalDateTime now = LocalDateTime.now();
        transaction.setCreatedAt(now);
        transaction.setCreatedBy(currentUser);
        transaction.setUpdatedAt(now);
        transaction.setUpdatedBy(currentUser);
        
        return transactionRepository.save(transaction);
    }

    /**
     * Retrieves all transactions.
     *
     * @return List of all transactions
     */
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    /**
     * Retrieves a transaction by its ID.
     *
     * @param id The ID of the transaction to retrieve
     * @return Optional containing the transaction if found
     */
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    /**
     * Retrieves transactions by account number.
     *
     * @param accountNumber The account number to search for
     * @return List of transactions for the given account
     */
    public List<Transaction> getTransactionsByAccountNumber(String accountNumber) {
        return transactionRepository.findByAccountNumber(accountNumber);
    }

    /**
     * Updates an existing transaction.
     *
     * @param id          The ID of the transaction to update
     * @param transaction The updated transaction data
     * @param currentUser The user performing the operation
     * @return The updated transaction
     */
    public Optional<Transaction> updateTransaction(Long id, Transaction transaction, String currentUser) {
        Optional<Transaction> existingTransaction = transactionRepository.findById(id);
        if (existingTransaction.isPresent()) {
            Transaction updated = existingTransaction.get();
            updated.setAmount(transaction.getAmount());
            updated.setCurrency(transaction.getCurrency());
            updated.setTransactionType(transaction.getTransactionType());
            updated.setAccountNumber(transaction.getAccountNumber());
            updated.setUpdatedAt(LocalDateTime.now());
            updated.setUpdatedBy(currentUser);
            
            return Optional.of(transactionRepository.save(updated));
        }
        return Optional.empty();
    }

    /**
     * Deletes a transaction by its ID.
     *
     * @param id The ID of the transaction to delete
     * @return True if deletion was successful, false otherwise
     */
    public boolean deleteTransaction(Long id) {
        int rowsAffected = transactionRepository.deleteById(id);
        return rowsAffected > 0;
    }
}
