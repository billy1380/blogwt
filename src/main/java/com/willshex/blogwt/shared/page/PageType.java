//
//  PageType.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.page;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.spacehopperstudios.utility.StringUtils;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.helper.RoleHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public enum PageType {
	SetupBlogPageType("setup", false),
	EditPostPageType("postedit", PermissionHelper.MANAGE_POSTS),
	PostsPageType("blog", false),
	PostDetailPageType("post", false),
	ChangeDetailsPageType("user", true),
	ChangePasswordPageType("changepassword", false),
	ResetPasswordPageType("resetpassword", false),
	RegisterPageType("register", false),
	LoginPageType("login", false),
	LogoutPageType("logout", true),
	PermissionsPageType("permissions", PermissionHelper.MANAGE_PERMISSIONS),
	RolesPageType("roles", PermissionHelper.MANAGE_ROLES),
	UsersPageType("users", PermissionHelper.MANAGE_USERS),
	PropertiesPageType("properties", RoleHelper.ADMIN),
	EditPagePageType("pageedit", PermissionHelper.MANAGE_PAGES),
	PageDetailPageType("page", false),
	PagesPageType("pages", PermissionHelper.MANAGE_PAGES),
	TagPostsPageType("tag", false),
	ResourcesPageType("resources", PermissionHelper.MANAGE_RESOURCES),
	SearchPostsPageType("search", false),
	VerifyAccountPageType("verifyaccount", false),
	ChangeAccessPageType("access", true),
	EditResourcePageType("resourceedit", PermissionHelper.MANAGE_RESOURCES),
	WidgetTestPageType("widgettest", true),
	AllPostsPageType("posts", PermissionHelper.MANAGE_POSTS),;

	private String value;
	private static Map<String, PageType> valueLookup = null;
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

		if (requiredPermissionCode != null
				&& requiredPermissionCode.length > 0) {
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
		return requiredPermissions == null ? null
				: requiredPermissions.values();
	}

	public Collection<String> getRequiredPermissionCodes () {
		return requiredPermissions == null ? null
				: requiredPermissions.keySet();
	}

	public boolean equals (String value) {
		value = stripExclamation(value);

		return this.value.equals(value);
	}

	private static String stripExclamation (String value) {
		if (value != null && value.length() > 0 && value.charAt(0) == '!') {
			value = value.substring(1);
		}

		return value;
	}
}
