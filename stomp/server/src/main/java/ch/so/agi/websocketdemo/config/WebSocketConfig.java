package ch.so.agi.websocketdemo.config;

import java.security.Principal;
import java.util.Map;

import org.apache.catalina.realm.GenericPrincipal;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setHandshakeHandler(new MyHandshakeHandler()); // Handshake and upgrade at http://localhost:8080/ws 
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic", "/queue");
    }
    
    @SuppressWarnings("deprecation")
	@Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(new ChannelInterceptorAdapter() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                
                System.out.println(accessor.toString());
                
                
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {

                    //UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("stefan", "fubar");
                   
                		Principal auth = new GenericPrincipal("stefan", "ziegler", null);
                		accessor.setUser(auth);
                }
                return message;
            }
        });
    }
    
    
    public class MyHandshakeHandler extends DefaultHandshakeHandler {

        @Override
        protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, 
                                          Map<String, Object> attributes) {
            // add your own code to determine the user
        	
        	
        	
    		System.out.println(request.getPrincipal());
    		System.out.println(request.getHeaders());
        	
            return null;
        }
    }
}

