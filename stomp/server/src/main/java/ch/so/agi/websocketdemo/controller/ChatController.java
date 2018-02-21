package ch.so.agi.websocketdemo.controller;

import ch.so.agi.websocketdemo.model.ChatMessage;
import ch.so.agi.websocketdemo.model.ChatMessage.MessageType;

import java.security.Principal;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
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
    //@SendTo("/user/queue/channel_pro_verheiratung")
    public ChatMessage fubar(Message<Object> message, @Payload ChatMessage chatMessage, 
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        //headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
    	
    		//System.out.println(message.getHeaders());
    		//System.out.println(SimpMessageHeaderAccessor.USER_HEADER);
    		
		//System.out.println(chatMessage.toString());
		//System.out.println(headerAccessor.getHeader("token"));  
		//System.out.println(headerAccessor.toNativeHeaderMap().toString());  
		//System.out.println(headerAccessor.toMessageHeaders().toString());  
		System.out.println(headerAccessor.getUser());
    	
		Principal user = headerAccessor.getUser();
		
		simpMessagingTemplate.convertAndSendToUser(headerAccessor.getUser().getName(), "/queue/channel_pro_verheiratung", chatMessage);
		
        return chatMessage;
    }
    
    
}
