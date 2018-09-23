/*
 * Copyright (C) 2013 , Inc. All rights reserved
 */
package com.pms.web.http.session.management;

import org.springframework.context.ApplicationListener;
import org.springframework.security.access.event.AuthorizationFailureEvent;

/**
 * The Class HttpSessionManagement.
 * 
 * 
 */
public class AuthorizationFailureEventListener implements ApplicationListener<AuthorizationFailureEvent> {

 // private static final Logger LOGGER = Logger.getLogger(AuthorizationFailureEventListener.class);

  /**
   * On application event.
   * 
   * @param event
   *          the event
   * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
   */
  @Override
  public void onApplicationEvent(final AuthorizationFailureEvent event) {
   // LOGGER.debug("AuthorizationFailureEvent:" + event.getSource());
  }

}
