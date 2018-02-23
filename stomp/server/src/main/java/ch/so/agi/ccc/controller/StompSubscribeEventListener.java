package ch.so.agi.ccc.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class StompSubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	
	private HashMap<String, ArrayList<String>> linkedClients = new HashMap<String, ArrayList<String>>();

	@Override
	public void onApplicationEvent(SessionSubscribeEvent sessionSubscribeEvent) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionSubscribeEvent.getMessage());

		String sessionId = headerAccessor.getNativeHeader("sessionId").get(0).toString();
		String client = headerAccessor.getNativeHeader("client").get(0).toString();

		log.info(headerAccessor.getNativeHeader("sessionId").get(0).toString());
		log.info(headerAccessor.getNativeHeader("client").get(0).toString());

		if (linkedClients.get(sessionId) == null) {
			log.info("Session ID nicht vorhanden.");
			log.info("Erster Client-Typ wird hinzugefügt: " + client);
			
			ArrayList<String> clients = new ArrayList<String>();
			clients.add(client);
			
			linkedClients.put(sessionId, clients);
		} else {
			log.info("Session ID bereits vorhanden.");
			ArrayList<String> subscribedClients = linkedClients.get(sessionId);
			
			log.info("Subscribed Client-Typ: " + subscribedClients.toString());
			
			// Prüfen, ob neu sub­skri­bie­render Client-Typ noch noch nicht vorhanden ist.
			// Es kann vorkommen, dass sich zwei "app" oder zwei "gis"
			// Klienten anmelden.
			if (!subscribedClients.contains(client)) {
				subscribedClients.add(client);
			};
		}
		
		// Double check. Es mindestens eine app- und eine gis-Verbindung.
		ArrayList<String> subscribedClients = linkedClients.get(sessionId);		
		if (subscribedClients.contains("app") && subscribedClients.contains("gis")) {
			log.info("app und gis haben sich subscribed.");
			
		}
		
		
		log.info(linkedClients.toString());

	}
}
