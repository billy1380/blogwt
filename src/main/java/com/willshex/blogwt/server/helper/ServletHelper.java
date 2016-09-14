//
//  ServletHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 5 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.willshex.blogwt.server.service.session.ISessionService;
import com.willshex.blogwt.server.service.session.SessionServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Session;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ServletHelper {

	/**
	 * @param request
	 * @return
	 */
	public static String constructBaseUrl (HttpServletRequest request) {
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();
		String url = scheme + "://" + serverName + ":" + serverPort;

		return url;
	}

	public static Session session (HttpServletRequest request) {
		ISessionService sessionService = SessionServiceProvider.provide();
		Cookie[] cookies = request.getCookies();
		String sessionId = null;
		Session userSession = null;

		if (cookies != null) {
			for (Cookie currentCookie : cookies) {
				if ("session.id".equals(currentCookie.getName())) {
					sessionId = currentCookie.getValue();
					break;
				}
			}

			if (sessionId != null) {
				userSession = sessionService
						.getSession(Long.valueOf(sessionId));

				if (userSession != null) {
					if (userSession.expires.getTime() > new Date().getTime()) {
						userSession = sessionService.extendSession(userSession,
								Long.valueOf(ISessionService.MILLIS_MINUTES));
						userSession.user = UserServiceProvider.provide()
								.getUser(userSession.userKey.getId());
						UserHelper.stripPassword(userSession.user);
						UserHelper.populateRolesAndPermissionsFromKeys(
								userSession.user);
					} else {
						sessionService.deleteSession(userSession);
						userSession = null;
					}
				}
			}
		}

		return userSession;
	}

}
