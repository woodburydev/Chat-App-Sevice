package com.codeforgeyt.wschatapplication.handlers;

import com.codeforgeyt.wschatapplication.config.ChatMessages;
import com.codeforgeyt.wschatapplication.dao.MessageDao;
import com.codeforgeyt.wschatapplication.dao.UserDao;
import com.codeforgeyt.wschatapplication.entities.ChatSessionResponse;
import com.codeforgeyt.wschatapplication.entities.Message;
import com.codeforgeyt.wschatapplication.entities.SessionUser;
import com.codeforgeyt.wschatapplication.entities.User;
import com.codeforgeyt.wschatapplication.security.MyUserDetailsService;
import com.codeforgeyt.wschatapplication.services.UserService;
import com.codeforgeyt.wschatapplication.util.JwtUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;


public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> webSocketSessions = new ArrayList<>();
    private final List<SessionUser> sessionUsers = new ArrayList<SessionUser>();

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        webSocketSessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Get payload and turn into a JSON Element
        String payload = message.getPayload();
        JsonElement element = JsonParser.parseString(payload);
        // Turn the json string into a JSON Element for Manipulation
        JsonObject jsonObj = element.getAsJsonObject();

        // Get the entries of the jsonObj.
        Set<Map.Entry<String, JsonElement>> entries = jsonObj.entrySet();

        // Map Through all Entries of the JSON Object { IDENTITY: ... , CHAT_MESSAGE: ... }
        for (Map.Entry<String, JsonElement> entry:entries) {

            String currentKey = entry.getKey();

            // Statement for Identity Message { IDENTITY... }
            if (currentKey.equals(ChatMessages.IDENTITY.name())) {

                // Get Values to Add To Session Users
                String chattingWith = jsonObj.get(ChatMessages.RECIPIENT.name()).getAsString();
                String currentUser = jsonObj.get(ChatMessages.IDENTITY.name()).getAsString();
                SessionUser newSessionUser = new SessionUser(currentUser, chattingWith, session.getId());
                sessionUsers.add(newSessionUser);

            }

            // Statement for Chat Message
            else if (currentKey.equals(ChatMessages.CHAT_MESSAGE.name())) {

                // Get the Author of the Chat Message from the Session Users
                SessionUser authorSession = getSessionUserById(session.getId());

                // Get the Recipient Info from Database
                String claimedRecipient = jsonObj.get(ChatMessages.RECIPIENT.name()).getAsString();
                User validatedRecipient = userDao.getUserByUsername(claimedRecipient);

                // Make Sure the Recipient Is In the Database
                if (validatedRecipient != null) {

                    // See If the Recipient Is In A Chat Session
                    SessionUser sessionRecipient = getSessionUserByUsername(validatedRecipient.getUsername());

                    boolean inSameChatSession = false;

                    if (sessionRecipient != null && sessionRecipient.getRecipient().equals(authorSession.getName())) {
                        inSameChatSession = true;
                    }

                    Message constructedMessage = new Message(
                            authorSession.getName(),
                            validatedRecipient.getUsername(),
                            jsonObj.get(ChatMessages.CHAT_MESSAGE.name()).getAsString(),
                            false,
                            UUID.randomUUID(),
                            Timestamp.from(Instant.now())
                            );

                    if (inSameChatSession) {

                        // Grab the Recipients Session
                        WebSocketSession recipientsSession = getWebSocketSession(sessionRecipient.getSessionId());
                        assert(recipientsSession != null);

                        // Set Read to True, Since They Are In the Same Session
                        constructedMessage.setRead(true);

                        // Going To Put Our Message In A Map to Be Identified By Client
                        ChatSessionResponse<Message> fullMessage = new ChatSessionResponse<>(ChatMessages.CHAT_MESSAGE, constructedMessage);

                        // Turn The Message to Json and Put Into Text Message Object
                        TextMessage constructedTextMessage = new TextMessage(new Gson().toJson(fullMessage));

                        // Ship off Messages
                        recipientsSession.sendMessage(constructedTextMessage);
                        session.sendMessage(constructedTextMessage);

                    } else {

                        // Put Message Into Hash Mash To Be Identified by Client
                        ChatSessionResponse<Message> fullMessage = new ChatSessionResponse<>(ChatMessages.CHAT_MESSAGE, constructedMessage);

                        // Convert to JSON and Send
                        TextMessage constructedTextMessage = new TextMessage(new Gson().toJson(fullMessage));
                        session.sendMessage(constructedTextMessage);
                    }

                    // Add This Message to Database
                    messageDao.addMessage(constructedMessage);

                } else {

                    // No User Exists in Database!
                    throw new Exception("No Recipients by that username");

                }
            }

            else if (currentKey.equals(ChatMessages.IS_TYPING.name())) {

                // Get the Author of the Chat Message from the Session Users
                SessionUser authorSession = getSessionUserById(session.getId());

                // Get Message from Client
                String isTypingMessage = jsonObj.get(ChatMessages.IS_TYPING.name()).getAsString();
                Boolean isTyping = Boolean.valueOf(isTypingMessage);

                // Get Recipient and Verify He Exists in Session Users
                String claimedRecipient = jsonObj.get(ChatMessages.RECIPIENT.name()).getAsString();
                SessionUser sessionRecipient = getSessionUserByUsername(claimedRecipient);

                // If He Is In The Session List and Talking To Us
                if (sessionRecipient != null && sessionRecipient.getRecipient().equals(authorSession.getName())) {

                    // Get Web Socket Session Of Recipient
                    WebSocketSession recipientsSession = getWebSocketSession(sessionRecipient.getSessionId());
                    assert(recipientsSession != null);

                    // Put Message Into A Map to Be Identified By The Client
                    ChatSessionResponse<Boolean> isTypingResponse = new ChatSessionResponse<>(ChatMessages.IS_TYPING, isTyping);

                    // Wrap Message in JSON & Send Off Message to Recipient
                    TextMessage constructedTextMessage = new TextMessage(new Gson().toJson(isTypingResponse));
                    recipientsSession.sendMessage(constructedTextMessage);

                }
            }

            else if (currentKey.equals(ChatMessages.AUTHENTICATION.name())) {

                String jwt = jsonObj.get(ChatMessages.AUTHENTICATION.name()).getAsString();
                SessionUser currentUser = getSessionUserById(session.getId());

                UserDetails userDetails = myUserDetailsService.loadUserByUsername(currentUser.getName());
                try {
                    jwtUtil.validateToken(jwt, userDetails);
                } catch(JwtException e) {
                    session.close();
                }

            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        SessionUser sessionUser = getSessionUserById(session.getId());
        sessionUsers.remove(sessionUser);
        webSocketSessions.remove(session);
    }

    private WebSocketSession getWebSocketSession(String sessionID) {
         return webSocketSessions
                .stream()
                .filter(item -> item.getId().equals(sessionID))
                .findFirst()
                .orElse(null);
    }

    private SessionUser getSessionUserById(String sessionId) {
        return sessionUsers.stream()
                .filter(item -> item.getSessionId().equals(sessionId))
                .findFirst()
                .orElse(null);
    }

    private SessionUser getSessionUserByUsername(String username) {
        return sessionUsers.stream()
                .filter(item -> item.getName().equals(username))
                .findFirst()
                .orElse(null);
    }
}
