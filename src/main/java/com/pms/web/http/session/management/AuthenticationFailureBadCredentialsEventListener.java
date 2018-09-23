/*
 * Copyright (C) 2013 , Inc. All rights reserved
 */
package com.pms.web.http.session.management;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;

/**
 * The Class HttpSessionManagement.
 * 
 * 
 */
public class AuthenticationFailureBadCredentialsEventListener implements
    ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

 // private static final Logger LOGGER = Logger.getLogger(AuthenticationFailureBadCredentialsEventListener.class);

  /**
   * On application event.
   * 
   * @param event
   *          the event
   * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
   */
  @Override
  public void onApplicationEvent(final AuthenticationFailureBadCredentialsEvent event) {
    //LOGGER.debug("AuthenticationFailureBadCredentialsEvent:" + event.getSource());
  }

}
