package com.example.finance2.config;

import com.example.finance2.model.Role;
import com.example.finance2.model.User;
import com.example.finance2.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

// Skapar demoanvändarna efter att schema.sql har byggt om tabellerna
@Component
public class DemoDataLoader implements CommandLineRunner {

    public static final String DEMO_PASSWORD = "demo123";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DemoDataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }
        String hash = passwordEncoder.encode(DEMO_PASSWORD);
        userRepository.save(new User("Darin", hash, new BigDecimal("100.00"), Role.USER));
        userRepository.save(new User("William", hash, new BigDecimal("500.00"), Role.ADMIN));
        userRepository.save(new User("Mikael", hash, new BigDecimal("50000.00"), Role.USER));
    }
}
