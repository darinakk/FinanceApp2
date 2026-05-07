package com.example.finance2.repository;

import com.example.finance2.model.User; // Se till att sökvägen till din User-klass stämmer
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Berättar för Spring att detta är klassen som sköter databasen
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    // Konstruktor: Spring skickar automatiskt in JdbcTemplate här
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Hitta en specifik person med hjälp av deras ID.
     */
    public User findUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        // queryForObject används när vi förväntar oss exakt ett svar
        return jdbcTemplate.queryForObject(sql, userRowMapper, id);
    }

    /**
     * Uppdatera saldot för en specifik person.
     */
    public void updateBalance(int id, double newBalance) {
        String sql = "UPDATE users SET balance = ? WHERE id = ?";

        // update används för kommandon som ändrar data (INSERT, UPDATE, DELETE)
        jdbcTemplate.update(sql, newBalance, id);
    }

    /**
     * Översättaren (RowMapper):
     * Beskriver hur en rad från MySQL (ResultSet) ska packas in i ett User-objekt.
     */
    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getInt("id"));        // Ta 'id' från SQL och sätt i Java-User
        user.setName(rs.getString("name"));   // Ta 'name' från SQL och sätt i Java-User
        user.setBalance(rs.getDouble("balance")); // Ta 'balance' från SQL och sätt i Java-User
        return user;
    };
}
