package com.example.finance2;

import com.example.finance2.Service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Finance2Application {

    public static void main(String[] args) {
        SpringApplication.run(Finance2Application.class, args);
    }

    @Bean
    public CommandLineRunner testBankLogic(UserService userService) {
        return args -> {
            System.out.println("\n--- STARTAR BANK-TESTER ---");

            // TEST 1: Ett godkänt uttag (William har 500 kr)
            System.out.println("Test 1: Försöker ta ut 200 kr från William...");
            userService.withdrawMoney(2, 200.0);

            // TEST 2: Ett för stort uttag (Ska misslyckas)
            System.out.println("\nTest 2: Försöker ta ut 10 000 kr från William...");
            userService.withdrawMoney(2, 10000.0);

            System.out.println("--- TESTER AVSLUTADE ---\n");
        };
    }
}

