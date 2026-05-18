package com.example.finance2.Repo;

// Vi ändrar till stort M för att matcha din Model-mapp
import com.example.finance2.Model.Role;
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

    public User findByName(String name) {
        try {
            String sql = "SELECT * FROM users WHERE name = ?";
            return jdbcTemplate.queryForObject(sql, userRowMapper, name);
        } catch (Exception e) {
            // Om ingen användare hittas returnerar vi null istället för att krascha
            return null;
        }
    }

    public User findUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, id);
    }



    public void updateBalance(int id, double newBalance) {
        String sql = "UPDATE users SET balance = ? WHERE id = ?";
        jdbcTemplate.update(sql, newBalance, id);
    }


    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setBalance(rs.getDouble("balance"));
        user.setPassword(rs.getString("password"));

        // Läser rollen och gör om texten från SQL till din Enum
        String roleStr = rs.getString("role");
        user.setRole(Role.valueOf(roleStr));

        return user;
    };
}