package com.example.finance2.service;

import com.example.finance2.exception.InsufficientFundsException;
import com.example.finance2.exception.InvalidAmountException;
import com.example.finance2.model.Transaction;
import com.example.finance2.model.TransactionType;
import com.example.finance2.model.User;
import com.example.finance2.repository.TransactionRepository;
import com.example.finance2.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    static final BigDecimal MAX_AMOUNT = new BigDecimal("1000000.00");

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public UserService(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public User getUser(String name) {
        return userRepository.findByName(name).orElseThrow();
    }

    // Saldoändringen och historikraden sparas i samma databastransaktion
    @Transactional
    public User deposit(String name, BigDecimal amount) {
        validateAmount(amount);
        User user = getUser(name);
        userRepository.increaseBalance(user.getId(), amount);
        transactionRepository.save(new Transaction(user.getId(), TransactionType.DEPOSIT, amount));
        return getUser(name);
    }

    @Transactional
    public User withdraw(String name, BigDecimal amount) {
        validateAmount(amount);
        User user = getUser(name);
        if (userRepository.decreaseBalanceIfSufficient(user.getId(), amount) == 0) {
            throw new InsufficientFundsException();
        }
        transactionRepository.save(new Transaction(user.getId(), TransactionType.WITHDRAWAL, amount));
        return getUser(name);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAllByOrderByIdDesc();
    }

    private static void validateAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new InvalidAmountException("Beloppet måste vara större än 0.");
        }
        if (amount.stripTrailingZeros().scale() > 2) {
            throw new InvalidAmountException("Beloppet får ha högst två decimaler.");
        }
        if (amount.compareTo(MAX_AMOUNT) > 0) {
            throw new InvalidAmountException("Beloppet får vara högst 1 000 000 kr per transaktion.");
        }
    }
}
