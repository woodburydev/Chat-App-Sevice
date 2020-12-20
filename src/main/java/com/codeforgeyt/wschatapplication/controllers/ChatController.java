package com.codeforgeyt.wschatapplication.controllers;

import com.codeforgeyt.wschatapplication.entities.Message;
import com.codeforgeyt.wschatapplication.services.ChatService;
import com.codeforgeyt.wschatapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController()
@RequestMapping("/chat")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;


    @GetMapping(value = "/messages", produces = "application/json")
    public List<Message> getAllMessages(@RequestParam(required = false) String user) {
        if (user != null) {
            return chatService.getMessagesBetweenUsers(userService.getLoggedInUser().getUsername(), user);
        }
        return chatService.getAllMessages();
    }

    @PostMapping(value = "/setToRead")
    public int setToRead(@RequestBody Map<String, UUID> jsonInput) {
        return chatService.setToRead(jsonInput.get("id"));
    }

    @GetMapping(value = "/getLastMessageRecieved")
    public Message getLastMessage(@RequestParam String recipientUsername) {
        return chatService.getLastMessage(recipientUsername);
    }

    @GetMapping(value = "/getLastMessageReadNow")
    public boolean getLastMessageReadNow(@RequestParam String recipientUsername) {
        Message lastChatMessage = chatService.getLastMessage(recipientUsername);

        if (lastChatMessage != null) {
            return lastChatMessage.getRead();
        }

        else {
            return false;
        }
    }

}
