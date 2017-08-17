//  
//  SessionService.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.session;

import static com.willshex.blogwt.server.helper.PersistenceHelper.keyToId;
import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Session;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.helper.DateTimeHelper;

final class SessionService implements ISessionService {

	public String getName () {
		return NAME;
	}

	public Session getSession (Long id) {
		return load().id(id.longValue()).now();
	}

	private LoadType<Session> load () {
		return provide().load().type(Session.class);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.services.session.ISessionService
	 * #addSession(com.willshex.blogwt.shared.api.datatypes.Session) */
	@Override
	public Session addSession (Session session) {
		if (session.created == null) {
			session.created = new Date();
		}

		session.userKey = Key.create(session.user);

		if (session.longTerm == null) {
			session.longTerm = Boolean.FALSE;
		}

		if (session.expires == null) {
			session.expires = DateTimeHelper.millisFromNow(
					Boolean.TRUE.equals(session.longTerm) ? MILLIS_DAYS
							: MILLIS_MINUTES);
		}

		Key<Session> key = provide().save().entity(session).now();
		session.id = keyToId(key);

		return session;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.services.session.ISessionService
	 * #updateSession(com.willshex.blogwt.shared.api.datatypes.Session ) */
	@Override
	public Session updateSession (Session session) {
		provide().save().entity(session);
		return session;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.services.session.ISessionService
	 * #deleteSession(com.willshex.blogwt.shared.api.datatypes.Session ) */
	@Override
	public void deleteSession (Session session) {
		provide().delete().entity(session);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.services.session.ISessionService
	 * #createUserSession(com.willshex.blogwt.shared.api.datatypes .User,
	 * java.lang.Boolean) */
	@Override
	public Session createUserSession (User user, Boolean longTerm) {
		return addSession(new Session().expires(DateTimeHelper.millisFromNow(
				Boolean.TRUE.equals(longTerm) ? MILLIS_DAYS : MILLIS_MINUTES))
				.longTerm(longTerm).user(user));
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.services.session.ISessionService
	 * #getUserSession(com.willshex.blogwt.shared.api.datatypes.User) */
	@Override
	public Session getUserSession (User user) {
		Session session = PersistenceHelper.one(load().filter("userKey", user));

		if (session != null
				&& session.expires.getTime() < new Date().getTime()) {
			deleteSession(session);
			session = null;
		}

		return session;
	}

	@Override
	public Session extendSession (Session session) {
		if (session.longTerm == null) {
			session.longTerm = Boolean.FALSE;
		}

		session.expires = DateTimeHelper.millisFromNow(
				Boolean.TRUE.equals(session.longTerm) ? MILLIS_DAYS
						: MILLIS_MINUTES);

		updateSession(session);

		UserServiceProvider.provide()
				.updateUserIdLastLoggedIn(keyToId(session.userKey));

		return session;
	}
}
