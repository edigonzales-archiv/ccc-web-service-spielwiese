package ch.so.agi.ccc.controller;

import ch.so.agi.ccc.model.CccMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
    		log.info("Received a new web socket connection");
    		log.info(event.getMessage().toString());
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        
        log.info("fubar");

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null) {
        	log.info("User Disconnected : " + username);

//            ChatMessage chatMessage = new ChatMessage();
//            chatMessage.setType(ChatMessage.MessageType.LEAVE);
//            chatMessage.setSender(username);
//
//            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}
