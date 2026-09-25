package com.example.finance2.controller;

import com.example.finance2.dto.AccountResponse;
import com.example.finance2.dto.AmountRequest;
import com.example.finance2.dto.TransactionResponse;
import com.example.finance2.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Alla konto-endpoints utgår från den inloggade användaren (Principal) istället för ett id i URL:en,
 * så ingen kan läsa eller ändra någon annans konto.
 */
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Används också vid inloggning: svarar 401 om användarnamn eller lösenord är fel
    @GetMapping("/me")
    public AccountResponse me(Principal principal) {
        return AccountResponse.from(userService.getUser(principal.getName()));
    }

    @PostMapping("/me/deposit")
    public AccountResponse deposit(Principal principal, @RequestBody AmountRequest request) {
        return AccountResponse.from(userService.deposit(principal.getName(), request.amount()));
    }

    @PostMapping("/me/withdraw")
    public AccountResponse withdraw(Principal principal, @RequestBody AmountRequest request) {
        return AccountResponse.from(userService.withdraw(principal.getName(), request.amount()));
    }

    // Endast ADMIN (se SecurityConfig)
    @GetMapping("/admin/transactions")
    public List<TransactionResponse> allTransactions() {
        return userService.getAllTransactions().stream().map(TransactionResponse::from).toList();
    }
}
