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

		if (session.expires == null) {
			session.expires = afterMinutes();
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

	private Date afterMinutes () {
		return afterMillis(MILLIS_MINUTES);
	}

	private Date afterDays () {
		return afterMillis(MILLIS_DAYS);
	}

	private Date afterMillis (Long millis) {
		return dateAfterMillis(new Date(), millis);
	}

	private Date dateAfterMillis (Date date, Long millis) {
		return new Date(date.getTime() + millis.longValue());
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
		return addSession(new Session()
				.expires(longTerm == null || !longTerm.booleanValue()
						? afterMinutes() : afterDays())
				.user(user));
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

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.session.ISessionService#extendSession
	 * (com.willshex.blogwt.shared.api.datatype.Session, java.lang.Long) */
	@Override
	public Session extendSession (Session session, Long duration) {
		if (session.expires == null) {
			session.expires = new Date();
		}

		if (duration == null) {
			duration = MILLIS_MINUTES;
		}

		session.expires = dateAfterMillis(session.expires, duration);

		updateSession(session);

		UserServiceProvider.provide()
				.updateUserIdLastLoggedIn(keyToId(session.userKey));

		return session;
	}
}