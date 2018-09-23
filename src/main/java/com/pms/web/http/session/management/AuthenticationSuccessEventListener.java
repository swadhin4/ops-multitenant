/*
 * Copyright (C) 2013 , Inc. All rights reserved
 */
package com.pms.web.http.session.management;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;

/*import com.service.NotificationService;*/

/**
 * The Class AuthenticationSuccessEventListener.
 * 
 * 
 */
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {


	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationSuccessEventListener.class);

	@Autowired
	private HttpSession session;

	/**
	 * On application event.
	 * 
	 * @param event
	 *          the event
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(final AuthenticationSuccessEvent event) {
		LOGGER.debug("AuthenticationSuccessEvent:" + event.getSource());
		if (event instanceof AuthenticationSuccessEvent)
		{
			AuthenticationSuccessEvent appEvent = event;
			UserDetails userDetails = (UserDetails) appEvent.getAuthentication().getPrincipal();
			LOGGER.debug("User " + userDetails.getUsername() +" is having roles as " + userDetails.getAuthorities());
		}
	}

}
