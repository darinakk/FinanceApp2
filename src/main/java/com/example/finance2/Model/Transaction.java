package com.example.finance2.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private String type; // "DEPOSIT" eller "WITHDRAW"
    private double amount;
    private LocalDateTime timestamp;

    // Tom konstruktor (behövs för JPA)
    public Transaction() {}

    // Konstruktor för att enkelt skapa nya transaktioner
    public Transaction(Long userId, String type, double amount) {
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    // Getters och Setters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setId(Long id) {
        this.id = id;
    }
}