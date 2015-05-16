//
//  PageType.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.History;
import com.spacehopperstudios.utility.StringUtils;
import com.willshex.blogwt.client.page.blog.EditPostPage;
import com.willshex.blogwt.client.page.blog.PostDetailPage;
import com.willshex.blogwt.client.page.blog.PostsPage;
import com.willshex.blogwt.client.page.blog.SetupBlogPage;
import com.willshex.blogwt.client.page.user.ChangeDetailsPage;
import com.willshex.blogwt.client.page.user.ChangePasswordPage;
import com.willshex.blogwt.client.page.user.LoginPage;
import com.willshex.blogwt.client.page.user.PermissionsPage;
import com.willshex.blogwt.client.page.user.RegisterPage;
import com.willshex.blogwt.client.page.user.RolesPage;
import com.willshex.blogwt.client.page.user.UsersPage;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.helper.PermissionHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public enum PageType {
	SetupBlogPageType("setup", false),
	EditPostPageType("blogedit", PermissionHelper.MANAGE_POSTS),
	PostsPageType("posts", false),
	PostDetailPageType("postdetail", false),
	ChangeDetailsPageType("userdetail", true),
	ChangePasswordPageType("changepassword", true),
	ResetPasswordPageType("resetpassword", true),
	RegisterPageType("register", false),
	LoginPageType("login", false),
	LogoutPageType("logout", true),
	PermissionsPageType("permissions", PermissionHelper.MANAGE_PERMISSIONS),
	RolesPageType("roles", PermissionHelper.MANAGE_ROLES),
	UsersPageType("users", PermissionHelper.MANAGE_USERS), ;

	private String value;
	private static Map<String, PageType> valueLookup = null;
	private PostsPage defaultPage = null;
	private Map<String, Permission> requiredPermissions;
	private boolean requiresAuthentication;

	private PageType (String value, boolean requiresAuthentication) {
		this.value = value;
		this.requiresAuthentication = requiresAuthentication;
	}

	private PageType (String value) {
		this.value = value;
		this.requiresAuthentication = false;
	}

	private PageType (String value, String... requiredPermissionCode) {
		this.value = value;
		this.requiresAuthentication = true;

		if (requiredPermissionCode != null && requiredPermissionCode.length > 0) {
			requiredPermissions = new HashMap<String, Permission>();

			Permission p;
			for (String code : requiredPermissionCode) {
				p = new Permission();
				p.code = code;
				requiredPermissions.put(code, p);
			}
		}
	}

	public String toString () {
		return value;
	}

	public String toString (String... params) {
		String asString;
		String joinedParams = StringUtils.join(Arrays.asList(params), "/");

		if (joinedParams == null || joinedParams.length() == 0) {
			asString = toString();
		} else {
			asString = toString() + "/" + joinedParams;
		}

		return asString;
	}

	public SafeUri asHref () {
		return UriUtils.fromString("#!" + toString());
	}

	public SafeUri asHref (String... params) {
		return UriUtils.fromString("#!" + toString(params));
	}

	public String asTargetHistoryToken () {
		return "!" + toString();
	}

	public String asTargetHistoryToken (String... params) {
		return "!" + toString(params);
	}

	public static PageType fromString (String value) {
		value = stripExclamation(value);

		if (valueLookup == null) {
			valueLookup = new HashMap<String, PageType>();

			for (PageType currentPageType : PageType.values()) {
				valueLookup.put(currentPageType.value, currentPageType);
			}
		}

		return value == null ? null : valueLookup.get(value);
	}

	public boolean requiresLogin () {
		return requiresAuthentication;
	}

	public Collection<Permission> getRequiredPermissions () {
		return requiredPermissions == null ? null : requiredPermissions
				.values();
	}

	public Collection<String> getRequiredPermissionCodes () {
		return requiredPermissions == null ? null : requiredPermissions
				.keySet();
	}

	public boolean equals (String value) {
		value = stripExclamation(value);

		return this.value.equals(value);
	}

	/**
	 * @return
	 */
	public Page create () {
		Page page = null;

		switch (this) {
		case UsersPageType:
			page = new UsersPage();
			break;
		case LoginPageType:
			page = new LoginPage();
			break;
		case RegisterPageType:
			page = new RegisterPage();
			break;
		case ChangePasswordPageType:
			page = new ChangePasswordPage();
			break;
		case RolesPageType:
			page = new RolesPage();
			break;
		case PermissionsPageType:
			page = new PermissionsPage();
			break;
		case ChangeDetailsPageType:
			page = new ChangeDetailsPage();
			break;
		case SetupBlogPageType:
			page = new SetupBlogPage();
			break;
		case EditPostPageType:
			page = new EditPostPage();
			break;
		case PostDetailPageType:
			page = new PostDetailPage();
			break;
		case LogoutPageType:
		case PostsPageType:
		default:
			if (defaultPage == null) {
				defaultPage = new PostsPage();
			}
			page = defaultPage;
			break;
		}

		return page;
	}

	public void show () {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute () {
				History.newItem(asTargetHistoryToken());
			}
		});
	}

	public void show (final String... params) {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute () {
				History.newItem(asTargetHistoryToken(params));
			}
		});
	}

	private static String stripExclamation (String value) {
		if (value != null && value.length() > 0 && value.charAt(0) == '!') {
			value = value.substring(1);
		}

		return value;
	}
}
