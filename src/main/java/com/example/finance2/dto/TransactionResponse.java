package com.example.finance2.dto;

import com.example.finance2.model.Transaction;
import com.example.finance2.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(Long id, Long userId, TransactionType type, BigDecimal amount, LocalDateTime createdAt) {

    public static TransactionResponse from(Transaction transaction) {
        return new TransactionResponse(transaction.getId(), transaction.getUserId(), transaction.getType(),
                transaction.getAmount(), transaction.getCreatedAt());
    }
}
