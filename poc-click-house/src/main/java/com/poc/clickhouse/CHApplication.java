package com.poc.clickhouse;

import com.poc.clickhouse.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CHApplication implements CommandLineRunner {

    private final TransactionService transactionService;

    @Autowired
    public CHApplication(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public static void main(String[] args) {
        SpringApplication.run(CHApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Initialize the database table
        transactionService.initializeDatabase();
    }
}

