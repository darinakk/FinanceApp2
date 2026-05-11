package com.example.finance2.Service;

import com.example.finance2.Model.User;
import com.example.finance2.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void depositMoney(int userId, double amount){
        User user = userRepository.findUserById(userId);

        double currentBalance = user.getBalance();
        double newBalance = currentBalance + amount;

        userRepository.updateBalance(userId, newBalance);

        System.out.println("Insättning klar" + user.getName() + "har nu" + newBalance + "kr.");
    }

    public void withdrawMoney(int userId, double amount){
        User user = userRepository.findUserById(userId);

        double currentBalance = user.getBalance();

        if (amount > currentBalance){

            System.out.println("MISSLYCKAT UTTAG: Användaren" + user.getName() + "har för lite pengar (Saldo:" + currentBalance + "kr).");
            return;
        }

        double newBalance = currentBalance - amount;

        userRepository.updateBalance(userId, newBalance);

        System.out.printf("UTTAG KLART %s tog ut %.2f kr. Nytt saldo: %.2f kr.%n", user.getName(), amount, newBalance);


    }
}
