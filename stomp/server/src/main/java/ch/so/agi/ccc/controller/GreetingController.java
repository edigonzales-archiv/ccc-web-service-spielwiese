package ch.so.agi.ccc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import ch.so.agi.ccc.model.CccMessage;
import ch.so.agi.ccc.model.Greeting;
import ch.so.agi.ccc.model.HelloMessage;

@Controller
public class GreetingController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());


    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + message.getName() + "!");
    }
    
    // One @MessageMapping for every application (= app & gis) using ccc.
    // You can grab @MessageMapping from the message header.
    @MessageMapping("/generic")
    @SendTo("/topic/greetings")
    public Greeting receiveMessage(@Payload CccMessage message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
    		MessageHeaders messageHeaders = headerAccessor.getMessageHeaders();
    		Map nativeHeaders = (Map) messageHeaders.get("nativeHeaders");
    	
    		// Which application send the message?
    		log.info("Application sent the message: " + nativeHeaders.get("destination").toString());
    	
    		log.info(headerAccessor.getMessageHeaders().toString());
    	
    	
    	
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + message.getApiVersion() + "!");
    }

}
