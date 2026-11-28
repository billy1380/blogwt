//
//  ServletHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 5 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import java.util.function.UnaryOperator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.service.session.ISessionService;
import com.willshex.blogwt.server.service.session.SessionServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Session;
import com.willshex.gson.web.service.server.ServiceException;
import com.willshex.server.ContextAwareServlet;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ServletHelper {

	public static final String UTF8 = "UTF-8";

	/** A site's own rule for its base URL, such as apex to www. */
	public static UnaryOperator<String> baseUrlCanonicalizer;

	/**
	 * @param request
	 * @return
	 */
	public static String constructBaseUrl (HttpServletRequest request) {
		return request.getScheme() + "://" + constructBaseAddress(request);
	}

	public static String canonicalBaseUrl (HttpServletRequest request) {
		String url = constructBaseUrl(request);
		return baseUrlCanonicalizer == null ? url
				: baseUrlCanonicalizer.apply(url);
	}

	/**
	 * @param request
	 * @return
	 */
	public static String constructBaseAddress (HttpServletRequest request) {
		int serverPort = request.getServerPort();
		String address = request.getServerName();

		if (isPortRequired(request.getScheme(), serverPort)) {
			address += ":" + serverPort;
		}

		return address;
	}

	private static boolean isPortRequired (String scheme, int port) {
		return (scheme.equalsIgnoreCase("https") && port != 443)
				|| (scheme.equalsIgnoreCase("http") && port != 80);
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

			if (sessionId != null && !sessionId.isEmpty()) {
				try {
					userSession = sessionService
							.getSession(Long.valueOf(sessionId));
				} catch (NumberFormatException e) {
					userSession = null;
				}

				if (userSession != null) {
					try {
						userSession = SessionValidator
								.lookupCheckAndExtend(userSession, "session");
						UserHelper.stripPassword(userSession.user);
						UserHelper.populateRolesAndPermissionsFromKeys(
								userSession.user);
					} catch (ServiceException e) {
						userSession = null;
					}
				}
			}
		}

		return userSession;
	}

	public static String constructBaseAddress () {
		return constructBaseAddress(ContextAwareServlet.REQUEST.get());
	}

	public static String constructBaseUrl () {
		HttpServletRequest request = ContextAwareServlet.REQUEST.get();
		return request.getScheme() + "://" + constructBaseAddress(request);
	}

}
