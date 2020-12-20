package com.codeforgeyt.wschatapplication.dao;

import com.codeforgeyt.wschatapplication.dto.RegisterRequest;
import com.codeforgeyt.wschatapplication.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User getUserByUsername(String username) {
        String sql = "select * from users where username=?;";
        UserMapper rowMapper = new UserMapper();
        return jdbcTemplate.queryForObject(sql, new Object[] {username}, rowMapper);
    }

    public SafeUser getSafeUserByUsername(String username) {
        User user = getUserByUsername(username);
        return new SafeUser(user.getUsername(), user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getLastRequest());
    }

    public SafeUser getSafeUserById(UUID id) {
        String sql = "select * from users where id = ?";
        SafeUserMapper rowMapper = new SafeUserMapper();
        return jdbcTemplate.queryForObject(sql, new Object[] {id}, rowMapper);
    }

    public int insertNewUser(RegisterRequest registerRequest) {
        String sql = "insert into users(ID, username, password, first_name, last_name, email, last_request) values (?, ?, ?, ?, ?, ?, ?)";
        try {
            String hashedPassword = BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt(10));
            jdbcTemplate.update(sql,
                    UUID.randomUUID(),
                    registerRequest.getUsername(),
                    hashedPassword,
                    registerRequest.getFirstName(),
                    registerRequest.getLastName(),
                    registerRequest.getEmail(),
                    Timestamp.from(Instant.now()));
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return 1;
    }

    public List<SafeUser> getAllUsers() {
        String sql = "select * from users;";
        SafeUserMapper rowMapper = new SafeUserMapper();
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void updateLastRequest(String username) {
        Instant now = Instant.now();
        Timestamp current = Timestamp.from(now);
        String sql = "update users set last_request=? where username=?;";
        try {
            jdbcTemplate.update(sql, current, username);
        }
        catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
