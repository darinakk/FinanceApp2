package com.example.finance2.repository;

import com.example.finance2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);

    // Saldot räknas om i databasen i ett enda UPDATE, så två samtidiga anrop
    // kan inte läsa samma gamla saldo och skriva över varandras ändringar.
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE User u SET u.balance = u.balance + :amount WHERE u.id = :id")
    int increaseBalance(@Param("id") Long id, @Param("amount") BigDecimal amount);

    // Uppdaterar bara om saldot räcker. Returnerar 0 om det inte gjorde det.
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE User u SET u.balance = u.balance - :amount WHERE u.id = :id AND u.balance >= :amount")
    int decreaseBalanceIfSufficient(@Param("id") Long id, @Param("amount") BigDecimal amount);
}
