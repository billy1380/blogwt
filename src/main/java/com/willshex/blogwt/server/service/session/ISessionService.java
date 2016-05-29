//  
//  ISessionService.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.session;

import com.willshex.blogwt.shared.api.datatype.Session;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.service.IService;

public interface ISessionService extends IService {
	public static final String NAME = "blogwt.session";
	
	public static final long MILLIS_MINUTES = 20l * 60l * 1000l;
	public static final long MILLIS_DAYS = 30l * 24l * 60l * 60l * 1000l;

	/**
	 * @param id
	 * @return
	 */
	public Session getSession(Long id);

	/**
	 * @param session
	 * @return
	 */
	public Session addSession(Session session);

	/**
	 * @param session
	 * @return
	 */
	public Session updateSession(Session session);

	/**
	 * @param session
	 */
	public void deleteSession(Session session);

	/**
	 * Create user session
	 * 
	 * @param user
	 * @param longTerm
	 * @return
	 */
	public Session createUserSession(User user, Boolean longTerm);

	/**
	 * Get user session
	 * 
	 * @param user
	 * @return
	 */
	public Session getUserSession(User user);

	/**
	 * @param session
	 * @param duration in milliseconds
	 * @return
	 */
	public Session extendSession(Session session, Long duration);

}