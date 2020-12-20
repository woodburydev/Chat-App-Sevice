package com.codeforgeyt.wschatapplication.dao;

import com.codeforgeyt.wschatapplication.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class MessageDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Message> getAllMessages() {
        String sql = "select * from messages;";
        return jdbcTemplate.query(sql, (item, index) ->
                new Message(item.getString("author"),
                        item.getString("recipient"),
                        item.getString("message"),
                        item.getBoolean("read"),
                        UUID.fromString(item.getString("ID")),
                        item.getTimestamp("timestamp")
                ));
    }

    public void addMessage(Message message) {
        String sql = "insert into messages(ID, author, recipient, message, read, timestamp) values (?, ?, ?, ?, ?, ?);";
        jdbcTemplate.update(sql, message.getID(), message.getAuthor(), message.getRecipient(), message.getMessage(), message.getRead(), message.getDate());
    }

    public List<Message> getMessagesBetweenUsers(String curUser, String username) {
        String sql = "select * from messages where author=? and recipient=? or author=? and recipient=?";

        return jdbcTemplate.query(sql, new Object[]{curUser, username, username, curUser}, (item, index) ->
                new Message(
                        item.getString("author"),
                        item.getString("recipient"),
                        item.getString("message"),
                        item.getBoolean("read"),
                        UUID.fromString(item.getString("ID")),
                        item.getTimestamp("timestamp")
                        ));
    }

    public int setToRead(UUID id) {
        String sql = "update messages set read = true where id=?;";
        try {
            jdbcTemplate.update(sql, id);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return 1;
    }

    public Message getLastMessage(String curUser, String recipientUsername) {
        String sql = "select timestamp, id, read, message, author, recipient from messages where author=? and recipient=? and timestamp=(select max(timestamp) from messages) " +
                "or author=? and recipient=? and timestamp=(select max(timestamp) from messages) limit 1;";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{recipientUsername, curUser, curUser, recipientUsername}, (item, index) ->
                    new Message(
                            item.getString("author"),
                            item.getString("recipient"),
                            item.getString("message"),
                            item.getBoolean("read"),
                            UUID.fromString(item.getString("ID")),
                            item.getTimestamp("timestamp")
                    ));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
