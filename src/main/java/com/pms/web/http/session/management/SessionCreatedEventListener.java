/*
 * Copyright (C) 2013 , Inc. All rights reserved
 */
package com.pms.web.http.session.management;

import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionCreationEvent;

/**
 * The Class SessionCreatedEventListener.
 * 
 * 
 */
public class SessionCreatedEventListener implements ApplicationListener<SessionCreationEvent> {

	// private static final Logger LOGGER = Logger.getLogger(SessionCreatedEventListener.class);

	/**
	 * On application event.
	 * 
	 * @param event
	 *          the event
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(final SessionCreationEvent event) {
		//LOGGER.debug("SessionCreationEvent:" + event.getSource());
	}

}
