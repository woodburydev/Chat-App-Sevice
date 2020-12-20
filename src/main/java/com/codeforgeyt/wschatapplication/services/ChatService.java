package com.codeforgeyt.wschatapplication.services;

import com.codeforgeyt.wschatapplication.dao.MessageDao;
import com.codeforgeyt.wschatapplication.entities.Message;
import com.codeforgeyt.wschatapplication.entities.SafeUser;
import com.codeforgeyt.wschatapplication.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChatService {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private UserService userService;

    public List<Message> getAllMessages() {
        return messageDao.getAllMessages();
    }

    public List<Message> getMessagesBetweenUsers(String curUser, String username) {
        return messageDao.getMessagesBetweenUsers(curUser, username);
    }

    public int setToRead(UUID id) {
        return messageDao.setToRead(id);
    }

    public Message getLastMessage(String recipientUsername) {
        SafeUser curUser = userService.getLoggedInUser();
        return messageDao.getLastMessage(curUser.getUsername(), recipientUsername);
    }
}
