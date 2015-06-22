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
import com.willshex.blogwt.shared.api.datatype.Session;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.json.service.server.InputValidationException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class SessionValidator {
	private static final String type = Session.class.getSimpleName();

	public static Session lookupAndExtend (Session session, String name)
			throws InputValidationException {
		Session lookupSession = lookup(session, name);

		Date now = new Date();
		if (lookupSession.expires.getTime() < now.getTime())
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.DataTypeNotFound, type + ": " + name);

		if ((lookupSession.expires.getTime() - now.getTime()) < ISessionService.MILLIS_MINUTES) {
			lookupSession = SessionServiceProvider.provide()
					.extendSession(lookupSession,
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
		if (session == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + ": " + name);

		boolean isIdLookup = false;

		if (session.id != null) {
			isIdLookup = true;
		}

		if (!isIdLookup)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.DataTypeNoLookup, type + ": " + name);

		Session lookupSession = null;
		if (isIdLookup) {
			lookupSession = SessionServiceProvider.provide().getSession(
					session.id);
		}

		if (lookupSession == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.DataTypeNotFound, type + ": " + name);

		return lookupSession;
	}

}
