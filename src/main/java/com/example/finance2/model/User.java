package com.example.finance2.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL sköter numreringen 1, 2, 3 själv
    private Long id;

    private String name;

    // BCrypt-hash av lösenordet, skickas aldrig ut från API:et
    private String password;

    // BigDecimal istället för double så att ören inte avrundas fel
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private Role role;

    // En tom konstruktor är ett krav för JPA
    protected User() {}

    public User(String name, String password, BigDecimal balance, Role role) {
        this.name = name;
        this.password = password;
        this.balance = balance;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Role getRole() {
        return role;
    }
}
