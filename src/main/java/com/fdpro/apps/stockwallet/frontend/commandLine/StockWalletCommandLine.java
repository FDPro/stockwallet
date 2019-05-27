package com.fdpro.apps.stockwallet.frontend.commandLine;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockWalletCommandLine implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(StockWalletCommandLine.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
