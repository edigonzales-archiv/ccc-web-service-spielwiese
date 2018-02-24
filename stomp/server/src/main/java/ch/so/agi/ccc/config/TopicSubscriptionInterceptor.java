package ch.so.agi.ccc.config;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

public class TopicSubscriptionInterceptor extends ChannelInterceptorAdapter {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // Hier kann unterbunden werden, dass sich mehrer app- oder mehrere gis-
    // Clients gleichezeitig mit der identischen Session-Id anmelden.
    // Prüfung kann beliebig sein, z.B. auch User/Login etc. wie hier
    // im Internet-Copy/Paste-Beispiel.
    //
    // Dieser ChannelInterceptor muss im WebSocketConfig angegeben werden.
    // 
    // Mit einem ChannelInterceptorAdapter kann wohl auch bei jeder Nachricht
    // geprüft werden, ob noch immer zwei verbunden sind.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {    	
        StompHeaderAccessor headerAccessor= StompHeaderAccessor.wrap(message);

        log.info(headerAccessor.getMessageHeaders().toString());

        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            Principal userPrincipal = headerAccessor.getUser();
            if(!validateSubscription(userPrincipal, headerAccessor.getDestination()))
            {
                // Es wird ein ERROR-Frame zurückgeschickt.
                throw new IllegalArgumentException("No permission for this topic");
            }
        }
        return message;
    }

    private boolean validateSubscription(Principal principal, String topicDestination) {
        if (principal == null) {
            // unauthenticated user
            return false;
        }
        log.debug("Validate subscription for {} to topic {}", principal.getName(), topicDestination);
        //Additional validation logic coming here
        return true;
    }
}