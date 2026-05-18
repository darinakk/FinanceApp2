package com.example.finance2.Service;

import com.example.finance2.Model.Transaction;
import com.example.finance2.Model.User;
import com.example.finance2.Repo.UserRepository;
import com.example.finance2.Repository.TransactionRepository; // Se till att namnet på mappen stämmer
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public UserService(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void depositMoney(int userId, double amount) {
        User user = userRepository.findUserById(userId);
        double newBalance = user.getBalance() + amount;
        userRepository.updateBalance(userId, newBalance);

        // Spara historik
        transactionRepository.save(new Transaction((long) userId, "DEPOSIT", amount));
    }

    @Transactional
    public boolean withdrawMoney(int userId, double amount) {
        User user = userRepository.findUserById(userId);
        if (amount > user.getBalance()) return false;

        double newBalance = user.getBalance() - amount;
        userRepository.updateBalance(userId, newBalance);

        // Spara historik
        transactionRepository.save(new Transaction((long) userId, "WITHDRAW", amount));
        return true;
    }

    public User getUserById(int id) {
        return userRepository.findUserById(id);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
    public User getUserByName(String name) {
        return userRepository.findByName(name);
    }
}