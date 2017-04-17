//
//  SessionValidator.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 15 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import java.util.Date;

import com.willshex.blogwt.server.service.session.ISessionService;
import com.willshex.blogwt.server.service.session.SessionServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Session;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.web.service.server.InputValidationException;
import com.willshex.gson.web.service.server.ServiceException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class SessionValidator extends ApiValidator {
	private static final String TYPE = Session.class.getSimpleName();

	public static Session lookupCheckAndExtend (Session session, String name)
			throws ServiceException {
		Session lookupSession = lookup(session, name);

		Date now = new Date();
		if (lookupSession.expires.getTime() < now.getTime())
			throwServiceError(InputValidationException.class,
					ApiError.DataTypeNotFound, TYPE + ": " + name);

		lookupSession.user = UserServiceProvider.provide()
				.getUser(Long.valueOf(lookupSession.userKey.getId()));

		UserValidator.suspended(lookupSession.user);

		if ((lookupSession.expires.getTime()
				- now.getTime()) < ISessionService.MILLIS_MINUTES) {
			lookupSession = SessionServiceProvider.provide().extendSession(
					lookupSession,
					Long.valueOf(ISessionService.MILLIS_MINUTES));
		}

		return lookupSession;
	}

	/**
	 * @param session
	 * @param name
	 * @return
	 * @throws InputValidationException 
	 */
	public static Session lookup (Session session, String name)
			throws InputValidationException {
		if (session == null) throwServiceError(InputValidationException.class,
				ApiError.InvalidValueNull, TYPE + ": " + name);

		boolean isIdLookup = false;

		if (session.id != null) {
			isIdLookup = true;
		}

		if (!isIdLookup) throwServiceError(InputValidationException.class,
				ApiError.DataTypeNoLookup, TYPE + ": " + name);

		Session lookupSession = null;
		if (isIdLookup) {
			lookupSession = SessionServiceProvider.provide()
					.getSession(session.id);
		}

		if (lookupSession == null)
			throwServiceError(InputValidationException.class,
					ApiError.DataTypeNotFound, TYPE + ": " + name);

		return lookupSession;
	}

}
