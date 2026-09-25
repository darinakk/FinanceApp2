package com.example.finance2;

import com.example.finance2.exception.InsufficientFundsException;
import com.example.finance2.model.Role;
import com.example.finance2.model.User;
import com.example.finance2.repository.TransactionRepository;
import com.example.finance2.repository.UserRepository;
import com.example.finance2.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

// Många samtidiga uttag från samma konto får aldrig göra saldot negativt.
// Med den gamla koden (läs saldo, räkna i Java, skriv tillbaka) kunde två uttag läsa samma saldo.
@SpringBootTest
class ConcurrentWithdrawalTest {

    private static final String NAME = "Parallell";

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private User account;

    @BeforeEach
    void createAccount() {
        account = userRepository.save(new User(NAME, "not-used", new BigDecimal("100.00"), Role.USER));
    }

    @AfterEach
    void deleteAccount() {
        transactionRepository.deleteAll(transactionRepository.findAll().stream()
                .filter(t -> t.getUserId().equals(account.getId()))
                .toList());
        userRepository.deleteById(account.getId());
    }

    @Test
    void concurrentWithdrawalsNeverOverdrawTheAccount() throws Exception {
        int attempts = 20; // 20 uttag à 10 kr från ett konto med 100 kr
        ExecutorService pool = Executors.newFixedThreadPool(attempts);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<Boolean>> results = new ArrayList<>();

        for (int i = 0; i < attempts; i++) {
            results.add(pool.submit(() -> {
                start.await();
                try {
                    userService.withdraw(NAME, new BigDecimal("10.00"));
                    return true;
                } catch (InsufficientFundsException e) {
                    return false;
                }
            }));
        }
        start.countDown();

        int succeeded = 0;
        for (Future<Boolean> result : results) {
            if (result.get(30, TimeUnit.SECONDS)) {
                succeeded++;
            }
        }
        pool.shutdown();

        assertThat(succeeded).isEqualTo(10);
        assertThat(userService.getUser(NAME).getBalance()).isEqualByComparingTo("0.00");
    }
}
