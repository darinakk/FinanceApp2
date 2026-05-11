package com.example.finance2.Service;

import com.example.finance2.Model.User;
import com.example.finance2.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * @Service markerar klassen som en Service-komponent i Spring.
 * Det är här "affärslogiken" bor – alltså reglerna för hur banken ska fungera.
 */
@Service
public class UserService {

    // Gör att referensen till repositoryt inte kan ändras efter start
    private final UserRepository userRepository;

    //Konstruktor för Dependency Injection. Spring skickar in UserRepository automatiskt.
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    /**
     * Metod för att sätta in pengar på en användares konto.
     */
    public void depositMoney(int userId, double amount){
        // 1. Hämta den senaste datan om användaren från databasen
        User user = userRepository.findUserById(userId);

        // 2. Räkna ut det nya saldot (nuvarande + insättning)
        double currentBalance = user.getBalance();
        double newBalance = currentBalance + amount;

        // 3. Be repositoryt att spara det nya saldot i databasen
        userRepository.updateBalance(userId, newBalance);

        // Skriver ut bekräftelse i konsolen (lade till mellanslag för snyggare utskrift)
        System.out.println("Insättning klar" + user.getName() + "har nu" + newBalance + "kr.");
    }

    /**
     * Metod för att ta ut pengar. Innehåller en viktig säkerhetskontroll.
     */
    public boolean withdrawMoney(int userId, double amount){
        // 1. Hämta användaren för att kontrollera nuvarande saldo
        User user = userRepository.findUserById(userId);
        double currentBalance = user.getBalance();

        // 2. Säkerhetskontroll: Man får inte ta ut mer än vad man har
        if (amount > currentBalance){

            // Om beloppet är för stort skriver vi ut ett fel och avbryter med 'return'
            System.out.println("MISSLYCKAT UTTAG: Användaren" + user.getName() + "har för lite pengar (Saldo:" + currentBalance + "kr).");
            return false;
        }

        // 3. Om det fanns tillräckligt med pengar: Räkna ut det nya saldot
        double newBalance = currentBalance - amount;

        // 4. Spara det nya saldot i databasen
        userRepository.updateBalance(userId, newBalance);

        //%.2f används för att visa saldot med två decimaler
        System.out.printf("UTTAG KLART %s tog ut %.2f kr. Nytt saldo: %.2f kr.%n", user.getName(), amount, newBalance);
        return true;


    }
    /**
     * Hämtar en specifik användare. Används av controllern för att visa data.
     */
    public User getUserById(int id){
        // Vi skickar bara anropet vidare till repositoryt som sköter SQL-frågan
        return userRepository.findUserById(id);
    }
}
