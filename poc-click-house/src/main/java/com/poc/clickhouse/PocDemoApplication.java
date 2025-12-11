package com.poc.clickhouse;

import com.poc.clickhouse.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class PocDemoApplication implements CommandLineRunner {

    private final TransactionService transactionService;

    @Autowired
    public PocDemoApplication(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public static void main(String[] args) {
        SpringApplication.run(PocDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Initialize the database table
        transactionService.initializeDatabase();
    }
}

