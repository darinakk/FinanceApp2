package com.example.finance2.repository;

// Vi ändrar till stort M för att matcha din Model-mapp
import com.example.finance2.Model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User findUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, id);
    }

    // OBS: Denna metod kommer inte fungera förrän du har
    // lagt till setBalance i User.java, så jag har kommenterat bort den.
    /*
    public void updateBalance(int id, double newBalance) {
        String sql = "UPDATE users SET balance = ? WHERE id = ?";
        jdbcTemplate.update(sql, newBalance, id);
    }
    */

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        // user.setBalance(rs.getDouble("balance")); <- Denna rad måste vara borttagen
        // eftersom metoden inte finns i din User-klass än.
        return user;
    };
}