/*
 * Copyright (C) 2013 , Inc. All rights reserved
 */
package com.pms.web.http.session.management;

import javax.servlet.http.HttpServletRequest;

import com.pms.jpa.entities.User;

/**
 * The Interface LogoutEventPublisher.
 *
 *
 */
public interface LogoutEventPublisher {

	/**
	 * Publish.
	 *
	 * @param request
	 *            the request
	 * @param user
	 *            the user
	 */
	public abstract void publish(HttpServletRequest request, User user);

}