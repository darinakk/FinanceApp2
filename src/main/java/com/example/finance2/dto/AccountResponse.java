package com.example.finance2.dto;

import com.example.finance2.model.Role;
import com.example.finance2.model.User;

import java.math.BigDecimal;

// Det som skickas till klienten. Lösenordshashen ingår medvetet inte.
public record AccountResponse(Long id, String name, BigDecimal balance, Role role) {

    public static AccountResponse from(User user) {
        return new AccountResponse(user.getId(), user.getName(), user.getBalance(), user.getRole());
    }
}
