/*
 * Copyright (C) 2013 , Inc. All rights reserved
 */
package com.pms.web.event;

import org.springframework.context.ApplicationEvent;

/**
 * The Class LogoutSuccessEvent.
 * 
 * @author Swadhin Mohanta
 */
public class LogoutSuccessEvent extends ApplicationEvent {

	/**
	 * Instantiates a new logout success event.
	 * 
	 * @param source
	 *            the source
	 */
	public LogoutSuccessEvent(final Object source) {
		super(source);
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3169048395799636911L;

}
