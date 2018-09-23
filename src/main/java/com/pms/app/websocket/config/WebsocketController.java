package com.pms.app.websocket.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketController.class);

	@MessageMapping("/notify")
	@SendTo("/topic/adminnotification")
	public NotifyMessage notifyMessage(final String notificationMsg) throws Exception {
		Thread.sleep(1000); // simulated delay
		LOGGER.info("WebsocketController - notifyMessage called.");
		LOGGER.info("Message Send : "+  notificationMsg);

		return new NotifyMessage(notificationMsg);
	}

}
