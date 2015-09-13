//
//  SessionController.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 15 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.user.UserService;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.client.helper.PageTypeHelper;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.datatype.Session;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.user.call.LoginRequest;
import com.willshex.blogwt.shared.api.user.call.LoginResponse;
import com.willshex.blogwt.shared.api.user.call.LogoutRequest;
import com.willshex.blogwt.shared.api.user.call.LogoutResponse;
import com.willshex.blogwt.shared.api.user.call.event.LoginEventHandler.LoginFailure;
import com.willshex.blogwt.shared.api.user.call.event.LoginEventHandler.LoginSuccess;
import com.willshex.blogwt.shared.api.user.call.event.LogoutEventHandler;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.helper.RoleHelper;
import com.willshex.blogwt.shared.page.PageType;
import com.willshex.gson.json.service.client.JsonService;
import com.willshex.gson.json.service.client.JsonServiceCallEventHandler;
import com.willshex.gson.json.service.shared.Response;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class SessionController implements JsonServiceCallEventHandler {
	private static final String COOKIE_KEY_ID = "session.id";

	private static SessionController one = null;

	public static SessionController get () {
		if (one == null) {
			one = new SessionController();
		}
		return one;
	}

	private Session session;

	private SessionController () {
		String sessionJson = jsonSession();

		if (sessionJson != null) {
			(session = new Session()).fromJson(sessionJson);
		} else {
			removeSession();
		}

		DefaultEventBus.get()
				.addHandler(JsonServiceCallEventHandler.TYPE, this);
	}

	public void login (String username, String password, boolean rememberMe) {
		UserService userService = ApiHelper.createUserClient();

		final LoginRequest input = ApiHelper.setAccessCode(new LoginRequest())
				.username(username).password(password)
				.longTerm(Boolean.valueOf(rememberMe));

		userService.login(input, createAsyncResponse(input));
	}

	public void restoreSession () {
		UserService userService = ApiHelper.createUserClient();

		final LoginRequest input = setSession(ApiHelper
				.setAccessCode(new LoginRequest()));

		userService.login(input, createAsyncResponse(input));
	}

	/**
	 * @param input
	 * @return
	 */
	private AsyncCallback<LoginResponse> createAsyncResponse (
			final LoginRequest input) {
		return new AsyncCallback<LoginResponse>() {

			@Override
			public void onSuccess (LoginResponse result) {
				if (result.status == StatusType.StatusTypeSuccess
						&& result.session != null) {
					session = result.session;
					Cookies.setCookie(COOKIE_KEY_ID, session.id.toString());

					if (user() != null && user().lastLoggedIn == null) {
						user().lastLoggedIn = new Date();
					}
				}

				DefaultEventBus.get()
						.fireEventFromSource(new LoginSuccess(input, result),
								SessionController.this);
			}

			@Override
			public void onFailure (Throwable caught) {
				DefaultEventBus.get()
						.fireEventFromSource(new LoginFailure(input, caught),
								SessionController.this);
			}
		};
	}

	public User user () {
		return session == null ? null : session.user;
	}

	public Session session () {
		return session;
	}

	public boolean isValidSession () {
		return sessionForApiCall() != null
				&& (session == null || (session.expires != null && session.expires
						.getTime() > (new Date()).getTime()));
	}

	public Session sessionForApiCall () {
		Session session = null;
		if (this.session != null && this.session.id != null) {
			session = new Session();
			session.id = this.session.id;
		} else {
			String id = Cookies.getCookie(COOKIE_KEY_ID);

			if (id != null) {
				session = new Session();
				session.id = Long.valueOf(id);
			}
		}

		return session;
	}

	public void logout () {
		logout(PageType.PostsPageType);
	}

	public void logout (PageType pageType, String... params) {
		UserService userService = ApiHelper.createUserClient();

		final LogoutRequest input = setSession(ApiHelper
				.setAccessCode(new LogoutRequest()));

		userService.logout(input, new AsyncCallback<LogoutResponse>() {

			@Override
			public void onSuccess (LogoutResponse output) {
				DefaultEventBus.get().fireEventFromSource(
						new LogoutEventHandler.LogoutSuccess(input, output),
						SessionController.this);
			}

			@Override
			public void onFailure (Throwable caught) {
				DefaultEventBus.get().fireEventFromSource(
						new LogoutEventHandler.LogoutFailure(input, caught),
						SessionController.this);
			}
		});

		session = null;
		Cookies.removeCookie(COOKIE_KEY_ID);
		if (pageType != null) {
			PageTypeHelper.show(pageType, params);
		}
	}

	/**
	 * @param input
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Request> T setSession (T input) {
		return (T) input.session(sessionForApiCall());
	}

	public boolean isAdmin () {
		return isValidSession() && user() != null && roles() != null
				&& RoleHelper.toLookup(roles()).containsKey(RoleHelper.ADMIN);
	}

	public List<Role> roles () {
		return user() == null || user().roles == null ? null : user().roles;
	}

	public List<Permission> permissions () {
		return user() == null || user().permissions == null ? null
				: user().permissions;
	}

	/**
	 * @param requiredPermissions
	 * @return
	 */
	public boolean isAuthorised (Permission... requiredPermissions) {
		return isAuthorised(Arrays.asList(requiredPermissions));
	}

	public boolean isAuthorised (Collection<Permission> requiredPermissions) {
		boolean authorised = isAdmin();

		if (!authorised && isValidSession() && user() != null
				&& permissions() != null) {
			if (requiredPermissions == null || requiredPermissions.size() == 0) {
				authorised = true;
			} else {
				Map<String, Permission> lookup = PermissionHelper
						.toLookup(permissions());
				for (Permission permission : requiredPermissions) {
					if (permission.code != null
							&& lookup.containsKey(permission.code)) {
						authorised = true;
						break;
					}
				}
			}
		}

		return authorised;
	}

	/**
	 * 
	 */
	public void removeSession () {
		removeJsonSession();
		session = null;
		Cookies.removeCookie(COOKIE_KEY_ID);
	}

	private static native String jsonSession ()
	/*-{
		return $wnd['session'];
	}-*/;

	private static native void removeJsonSession ()
	/*-{
		$wnd['session'] = null;
	}-*/;

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.json.service.client.JsonServiceCallEventHandler#
	 * jsonServiceCallStart(com.willshex.gson.json.service.client.JsonService,
	 * java.lang.String, com.willshex.gson.json.service.shared.Request,
	 * com.google.gwt.http.client.Request) */
	@Override
	public void jsonServiceCallStart (JsonService origin, String callName,
			com.willshex.gson.json.service.shared.Request input,
			com.google.gwt.http.client.Request handle) {
		GWT.log("Calling " + origin.getUrl() + "." + callName + " with input ["
				+ input + "].");
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.json.service.client.JsonServiceCallEventHandler#
	 * jsonServiceCallSuccess(com.willshex.gson.json.service.client.JsonService,
	 * java.lang.String, com.willshex.gson.json.service.shared.Request,
	 * com.willshex.gson.json.service.shared.Response) */
	@Override
	public void jsonServiceCallSuccess (JsonService origin, String callName,
			com.willshex.gson.json.service.shared.Request input, Response output) {
		GWT.log("Call to " + origin.getUrl() + "." + callName + " with input ["
				+ input + "] succeeded with output [" + output + "].");
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.json.service.client.JsonServiceCallEventHandler#
	 * jsonServiceCallFailure(com.willshex.gson.json.service.client.JsonService,
	 * java.lang.String, com.willshex.gson.json.service.shared.Request,
	 * java.lang.Throwable) */
	@Override
	public void jsonServiceCallFailure (JsonService origin, String callName,
			com.willshex.gson.json.service.shared.Request input,
			Throwable caught) {
		GWT.log("Call to " + origin.getUrl() + "." + callName + " with input ["
				+ input + "] failed.", caught);
	}
}
