package com.example.finance2.Controller;

import com.example.finance2.Model.LoginRequest;
import com.example.finance2.Model.User;
import com.example.finance2.Service.UserService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/login")
    public User login(@RequestBody LoginRequest loginData) {
        User user = userService.getUserByName(loginData.getUsername());
        if (user != null && user.getPassword().equals(loginData.getPassword())) {
            return user; // Returnerar hela användaren om lösenordet stämmer
        }
        return null;
    }
}