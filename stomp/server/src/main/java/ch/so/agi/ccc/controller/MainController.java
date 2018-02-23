package ch.so.agi.ccc.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import ch.so.agi.ccc.model.CccMessage;
import ch.so.agi.ccc.model.Greeting;
import ch.so.agi.ccc.model.HelloMessage;

@Controller
public class MainController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + message.getName() + "!");
    }
    
    // Jede Fachapplikation-WebGIS-Kombination erhält einen eigenen
    // Endpunkt (=@MessageMapping), damit die Requests etc. einer  
    // Kombination zugeordnet werden kann.
    @MessageMapping({"/generic", "/baugk-afu"})
    public void receiveMessage(@Payload CccMessage message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
    		MessageHeaders messageHeaders = headerAccessor.getMessageHeaders();
    		//Map nativeHeaders = (Map) messageHeaders.get("nativeHeaders");
    	
    		String destination = (String) headerAccessor.getMessageHeaders().get("simpDestination");
    		log.info("Requested endpoint: " + destination);
    		log.info(headerAccessor.getMessageHeaders().toString());
    		
    		String sessionId = message.getSessionId();
    	
        Thread.sleep(250); // simulated delay
		simpMessagingTemplate.convertAndSend("/queue/ccci_" + sessionId, new Greeting("Hello, " + message.getSessionId() + " | " + message.getSender()));
    }
}
