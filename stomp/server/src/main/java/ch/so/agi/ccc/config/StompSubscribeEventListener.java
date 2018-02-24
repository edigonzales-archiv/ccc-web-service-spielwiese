package ch.so.agi.ccc.config;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import ch.so.agi.ccc.model.CccMessage;
import ch.so.agi.ccc.model.CccMessage.SenderType;

// Behandelt subscribe events. Analog kann man mit session connect/disconnetc und session unsubscribe
// die benötigte Logik abhandeln.
// https://docs.spring.io/autorepo/docs/spring/4.3.1.RELEASE/javadoc-api/org/springframework/web/socket/messaging/AbstractSubProtocolEvent.html
@Component
public class StompSubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    private HashMap<String, ArrayList<String>> linkedClients = new HashMap<String, ArrayList<String>>();
    
    // Bei einem SessionSubscibeEvent wird geprüft, ob sich je ein app-
    // und ein gis-Client mit gleicher Session-ID angemeldet haben.
    // Wenn das der Fall ist, wird die "ready"-Nachricht verschicht.
    //
    // Hier wird nicht verhindert, dass sich mehrere app- oder mehrere
    // gis-Clients mit der gleichen Session-ID subskribieren können. 
    // Soll das nicht der Falls sein, muss dies in einem ChannelInterceptorAdapter
    // behandelt werden.
    //
    // Wenn sich ein weiterer Client subskribiert (egal app oder gis) wird nochmals
    // eine ready-Nachricht verschickt.
    @Override
    public void onApplicationEvent(SessionSubscribeEvent sessionSubscribeEvent) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionSubscribeEvent.getMessage());

        String sessionId = headerAccessor.getNativeHeader("sessionId").get(0).toString();
        String client = headerAccessor.getNativeHeader("client").get(0).toString();

        log.info(headerAccessor.getNativeHeader("sessionId").get(0).toString());
        log.info(headerAccessor.getNativeHeader("client").get(0).toString());

        if (linkedClients.get(sessionId) == null) {
            log.info("Session-ID nicht vorhanden.");
            log.info("Erster Client-Typ wird hinzugefügt: " + client);
            
            ArrayList<String> clients = new ArrayList<String>();
            clients.add(client);
            
            linkedClients.put(sessionId, clients);
        } else {
            
            log.info("Session-ID bereits vorhanden.");
            ArrayList<String> subscribedClients = linkedClients.get(sessionId);
            
            log.info("Subskribierender Client-Typ: " + subscribedClients.toString());
            
            // Prüfen, ob neu sub­skri­bie­render Client-Typ noch noch nicht vorhanden ist.
            // Es kann vorkommen, dass sich zwei "app" oder zwei "gis"
            // Klienten anmelden.
            if (!subscribedClients.contains(client)) {
                subscribedClients.add(client);
            };
        }
        
        // Double check. Es gibt mindestens eine app- und eine gis-Verbindung.
        // Entspricht Subskribtion mit gleicher Session-ID.
        ArrayList<String> subscribedClients = linkedClients.get(sessionId);		
        if (subscribedClients.contains("app") && subscribedClients.contains("gis")) {
            log.info(String.format("app- und gis-Client mit gemeinsamer Session-ID (%s) haben sich subskribiert. Ready-Nachricht wird verschickt.", sessionId));		
            log.info(linkedClients.toString());
            
            CccMessage message = new CccMessage();
            message.setReady(true);
            message.setSender(SenderType.SERVER);
            message.setApiVersion("1.0");
            simpMessagingTemplate.convertAndSend("/queue/ccc_" + sessionId, message);
        }
    }
}
