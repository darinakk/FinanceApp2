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
    public CommandLineRunner testDeposit(UserService userService) {
        return args -> {
            System.out.println("--- STARTAR TEST AV INSÄTTNING ---");

            // Vi testar att sätta in 500 kr på användare med ID 1 (Darin)
            // Kolla i din schema.sql vilket ID som skapades först
            try {
                userService.depositMoney(1, 500.0);
                System.out.println("Testet lyckades!");
            } catch (Exception e) {
                System.out.println("Testet misslyckades: " + e.getMessage());
            }

            System.out.println("--- TEST AVSLUTAT ---");
        };
    }
}

