package com.codeforgeyt.wschatapplication.entities;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SafeUserMapper implements RowMapper<SafeUser> {

    public SafeUser mapRow(ResultSet rs, int i) throws SQLException {
        return new SafeUser(
                rs.getString("username"),
                UUID.fromString(rs.getString("ID")),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getTimestamp("last_request")
        );
    }
}
