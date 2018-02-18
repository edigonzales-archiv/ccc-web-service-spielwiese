package ch.so.agi.websocketdemo.controller;

import ch.so.agi.websocketdemo.model.ChatMessage;
import ch.so.agi.websocketdemo.model.ChatMessage.MessageType;

import java.text.SimpleDateFormat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, 
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage send(@Payload ChatMessage chatMessage) throws Exception {
    		return chatMessage;
    }
    
    @MessageMapping("/fubar")
    @SendTo("/user/queue/reply")
    public ChatMessage fubar(@Payload ChatMessage chatMessage, 
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        //headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
    	
    	
		System.out.println(chatMessage.toString());
		//System.out.println(headerAccessor.getHeader("token"));  
		System.out.println(headerAccessor.toNativeHeaderMap().toString());  
		System.out.println(headerAccessor.toMessageHeaders().toString());  
		//System.out.println(headerAccessor.getUser(headers));
    	
        return chatMessage;
    }
    
    
}
