package com.example.finance2.repository;

import com.example.finance2.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Nyaste först
    List<Transaction> findAllByOrderByIdDesc();
}
