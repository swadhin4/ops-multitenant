/*
 * Copyright (C) 2013 , Inc. All rights reserved
 */
package com.pms.web.http.session.management;

import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionDestroyedEvent;

/**
 * The Class HttpSessionManagement.
 * 
 * 
 */
public class SessionDestroyedEventListener implements ApplicationListener<SessionDestroyedEvent> {

  //private static final Logger LOGGER = Logger.getLogger(SessionDestroyedEventListener.class);

  /**
   * On application event.
   * 
   * @param event
   *          the event
   * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
   */
  @Override
  public void onApplicationEvent(final SessionDestroyedEvent event) {
   // LOGGER.debug("SessionDestroyedEvent:" + event.getSource());
  }

}
