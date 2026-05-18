package com.example.finance2.Controller;

import com.example.finance2.Model.User;
import com.example.finance2.Service.UserService;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:63342")

/**
 * @RestController talar om för Spring att den här klassen ska hantera HTTP-anrop.
 * Den skickar automatiskt tillbaka data (som t.ex. User-objekt) i JSON-format.
 */
@RestController
public class UserController {
    // Vi skapar en referens till vår Service som innehåller all logik
    private final UserService userService;

    // Konstruktor för att injicera UserService (Dependency Injection)
    public UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * Endpoint för att hämta information om en specifik användare.
     * Webbadress: GET http://localhost:8080/users/1
     */
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable int id){
        return userService.getUserById(id);
    }

    /**
     * Endpoint för att sätta in pengar på ett konto.
     * Vi använder POST eftersom vi ändrar data i systemet.
     * Webbadress: POST http://localhost:8080/users/1/add?amount=500
     */
    @PostMapping("/users/{id}/add")
    public String deposit(@PathVariable int id, @RequestParam double amount){
        // @RequestParam plockar ut värdet från URL-parametern (?amount=xxx)

        // Vi anropar servicen som sköter beräkningen och uppdaterar databasen
        userService.depositMoney(id, amount);
        // Vi returnerar en textbekräftelse till den som anropade API:et
        return "Insättning på " + amount + " kr lyckades för användare " + id;
    }

    /**
     * Endpoint för att ta ut pengar.
     * Webbadress: POST http://localhost:8080/users/1/withdraw?amount=200
     */
    @PostMapping("/users/{id}/withdraw")
    public String withdraw(@PathVariable int id, @RequestParam double amount) {
        // Vi frågar servicen om uttaget går att genomföra
        boolean success = userService.withdrawMoney(id, amount);

        if (success) {
            return "Uttag på " + amount + " kr lyckades!";
        } else {
            // Här fångar vi upp om servicen sa 'false' (inte tillräckligt med pengar)
            return "Uttaget misslyckades: Du har inte tillräckligt med pengar på kontot.";
        }
    }

}
