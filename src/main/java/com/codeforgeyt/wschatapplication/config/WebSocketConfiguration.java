package com.codeforgeyt.wschatapplication.config;

import com.codeforgeyt.wschatapplication.handlers.ChatWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final static String CHAT_ENDPOINT = "/chat";

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(getChatWebSocketHandler(), CHAT_ENDPOINT).setAllowedOrigins("http://woodburydev-chat-application.herokuapp.com");
    }

    @Bean
    public ChatWebSocketHandler getChatWebSocketHandler(){
        return new ChatWebSocketHandler();
    }
}
