package com.poc.clickhouse.repository;


import com.poc.clickhouse.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for managing Transaction entities in ClickHouse.
 * Provides CRUD operations and custom queries for financial transactions.
 */
@Repository
public class TransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Row mapper for converting ResultSet to Transaction entity.
     */
    private static class TransactionRowMapper implements RowMapper<Transaction> {
        @Override
        public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            Transaction transaction = new Transaction();
            transaction.setId(rs.getLong("id"));
            transaction.setAmount(rs.getDouble("amount"));
            transaction.setCurrency(rs.getString("currency"));
            transaction.setTransactionType(rs.getString("transaction_type"));
            transaction.setAccountNumber(rs.getString("account_number"));
            
            // Handle timestamp fields
            transaction.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
            transaction.setCreatedBy(rs.getString("created_by"));
            transaction.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
            transaction.setUpdatedBy(rs.getString("updated_by"));
            
            return transaction;
        }
    }

    /**
     * Creates the transactions table if it doesn't exist.
     */
    public void initializeTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS transactions (
                id UInt64,
                amount Float64,
                currency String,
                transaction_type String,
                account_number String,
                created_at DateTime DEFAULT now(),
                created_by String,
                updated_at DateTime DEFAULT now(),
                updated_by String
            ) ENGINE = MergeTree()
            ORDER BY (id, created_at)
            """;
        
        jdbcTemplate.execute(sql);
    }

    /**
     * Saves a new transaction to the database.
     *
     * @param transaction The transaction to save
     * @return The saved transaction with generated ID
     */
    public Transaction save(Transaction transaction) {
        String sql = """
            INSERT INTO transactions 
            (id, amount, currency, transaction_type, account_number, created_at, created_by, updated_at, updated_by)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        // In a real implementation, you would generate IDs properly
        // For demo purposes, we're using a simple approach
        long id = System.currentTimeMillis(); // Simple ID generation for demo
        transaction.setId(id);

        jdbcTemplate.update(sql,
                transaction.getId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getTransactionType(),
                transaction.getAccountNumber(),
                transaction.getCreatedAt() != null ? transaction.getCreatedAt() : LocalDateTime.now(),
                transaction.getCreatedBy() != null ? transaction.getCreatedBy() : "system",
                transaction.getUpdatedAt() != null ? transaction.getUpdatedAt() : LocalDateTime.now(),
                transaction.getUpdatedBy() != null ? transaction.getUpdatedBy() : "system");

        return transaction;
    }

    /**
     * Finds all transactions in the database.
     *
     * @return List of all transactions
     */
    public List<Transaction> findAll() {
        String sql = "SELECT * FROM transactions ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new TransactionRowMapper());
    }

    /**
     * Finds a transaction by its ID.
     *
     * @param id The ID of the transaction to find
     * @return Optional containing the transaction if found
     */
    public Optional<Transaction> findById(Long id) {
        String sql = "SELECT * FROM transactions WHERE id = ? LIMIT 1";
        List<Transaction> results = jdbcTemplate.query(sql, new TransactionRowMapper(), id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    /**
     * Finds transactions by account number.
     *
     * @param accountNumber The account number to search for
     * @return List of transactions for the given account
     */
    public List<Transaction> findByAccountNumber(String accountNumber) {
        String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new TransactionRowMapper(), accountNumber);
    }

    /**
     * Deletes a transaction by its ID.
     *
     * @param id The ID of the transaction to delete
     * @return Number of rows affected
     */
    public int deleteById(Long id) {
        String sql = "ALTER TABLE transactions DELETE WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
