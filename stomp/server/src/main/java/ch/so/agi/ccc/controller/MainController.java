package ch.so.agi.ccc.controller;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import ch.so.agi.ccc.model.CccMessage;
import ch.so.agi.ccc.model.CccMessage.SenderType;

@Controller
public class MainController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    // Jede Fachapplikation-WebGIS-Kombination erhält einen eigenen
    // Endpunkt (=@MessageMapping), damit die Requests etc. einer  
    // Kombination zugeordnet werden kann.
    //@MessageMapping({"/generic", "/baugk-afu"})
    @MessageMapping("/{channelId}")
    public void receiveMessage(@DestinationVariable String channelId, @Payload CccMessage message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        log.info("[{}] : " + headerAccessor.getMessageHeaders().toString(), channelId);

        String sessionId = headerAccessor.getNativeHeader("sessionId").get(0).toString();
        String client = headerAccessor.getNativeHeader("client").get(0).toString();

        // Sender der Nachricht in Message verpacken, damit Clienten
        // einfach entscheiden können, ob sie was zu tun haben oder nicht.
        // Analog dem Senden könnte man das in den Header packen.
        // Schaff ich aber nicht. Verstehe es aber nicht wirklich...
        message.setSender(SenderType.valueOf(client.toUpperCase()));
        //message.setReady(null);

        log.info(message.toString());

        Thread.sleep(250); // simulated delay

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("client", client);
        headers.put("sessionId", sessionId);

        simpMessagingTemplate.setDefaultDestination("/queue/ccc_" + sessionId);
        //simpMessagingTemplate.convertAndSend(message, headers); // Warum geht das nicht?
        simpMessagingTemplate.convertAndSend(message);
    }
}
