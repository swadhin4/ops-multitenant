package com.pms.app.websocket.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketConfig.class);
	@Override
	public void configureMessageBroker(final MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
		config.setApplicationDestinationPrefixes("/pms");
		LOGGER.info("WebSocketConfig - configureMessageBroker called");
	}

	@Override
	public void registerStompEndpoints(final StompEndpointRegistry registry) {
		LOGGER.info("WebSocketConfig - registerStompEndpoints called");
		registry.addEndpoint("/notify-socket").withSockJS();
	}

}