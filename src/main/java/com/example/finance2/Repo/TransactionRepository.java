package com.example.finance2.Repository;

import com.example.finance2.Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Denna gör att vi kan hitta alla transaktioner för en specifik användare
    List<Transaction> findByUserId(Long userId);
}