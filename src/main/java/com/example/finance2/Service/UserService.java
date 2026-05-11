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

}
