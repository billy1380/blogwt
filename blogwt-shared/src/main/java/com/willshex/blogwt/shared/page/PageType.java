//
//  PageType.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.page;

import static com.willshex.blogwt.shared.helper.PermissionHelper.MANAGE_NOTIFICATIONS;
import static com.willshex.blogwt.shared.helper.PermissionHelper.MANAGE_PAGES;
import static com.willshex.blogwt.shared.helper.PermissionHelper.MANAGE_PERMISSIONS;
import static com.willshex.blogwt.shared.helper.PermissionHelper.MANAGE_POSTS;
import static com.willshex.blogwt.shared.helper.PermissionHelper.MANAGE_RESOURCES;
import static com.willshex.blogwt.shared.helper.PermissionHelper.MANAGE_ROLES;
import static com.willshex.blogwt.shared.helper.PermissionHelper.MANAGE_USERS;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.utility.StringUtils;

/**
 * @author William Shakour (billy1380)
 *
 */
public enum PageType {
	SetupBlogPageType("setup"),
	EditPostPageType("postedit", PermissionHelper.create(MANAGE_POSTS)),
	PostsPageType("blog"),
	PostDetailPageType("post"),
	ChangeDetailsPageType("user", true),
	ChangePasswordPageType("changepassword"),
	ResetPasswordPageType("resetpassword"),
	RegisterPageType("register"),
	LoginPageType("login"),
	LogoutPageType("logout", true),
	PermissionsPageType("permissions",
			PermissionHelper.create(MANAGE_PERMISSIONS)),
	RolesPageType("roles", PermissionHelper.create(MANAGE_ROLES)),
	UsersPageType("users", PermissionHelper.create(MANAGE_USERS)),
	PropertiesPageType("properties", true, true),
	EditPagePageType("pageedit", PermissionHelper.create(MANAGE_PAGES)),
	PageDetailPageType("page"),
	PagesPageType("pages", PermissionHelper.create(MANAGE_PAGES)),
	TagPostsPageType("tag"),
	ResourcesPageType("resources", PermissionHelper.create(MANAGE_RESOURCES)),
	SearchPostsPageType("search"),
	VerifyAccountPageType("verifyaccount"),
	ChangeAccessPageType("access", true),
	EditResourcePageType("resourceedit",
			PermissionHelper.create(MANAGE_RESOURCES)),
	WidgetTestPageType("widgettest", true),
	AllPostsPageType("posts", PermissionHelper.create(MANAGE_POSTS)),
	DownloadsPageType("downloads",
			new Property().name(PropertyHelper.DOWNLOAD_ENABLED)
					.value(Boolean.toString(true))),
	MetaNotificationsPageType("metanotifications",
			PermissionHelper.create(MANAGE_NOTIFICATIONS)),
	MetaNotificationDetailPageType("metanotificationdetail",
			PermissionHelper.create(MANAGE_NOTIFICATIONS)),;

	private String value;
	private static Map<String, PageType> valueLookup = null;
	private Map<String, Permission> requiredPermissions;
	private Map<String, Property> requiredProperties;
	private boolean requiresAuthentication;
	private boolean requiresSystemAdmin;

	private PageType (String value) {
		this(value, false, false);
	}

	private PageType (String value, boolean requiresAuthentication,
			boolean requiresSystemAdmin) {
		this.value = value;
		this.requiresAuthentication = requiresAuthentication;
		this.requiresSystemAdmin = requiresSystemAdmin;
	}

	private PageType (String value, Collection<Permission> requiresPermission) {
		this(value, true, requiresPermission, null);
	}

	private PageType (String value, Permission... requiresPermission) {
		this(value, true, Arrays.asList(requiresPermission), null);
	}

	private PageType (String value, boolean requiresAuthentication,
			Property... requiredProperty) {
		this(value, requiresAuthentication, null, requiredProperty);
	}

	private PageType (String value, Property... requiredProperty) {
		this(value, false, null, requiredProperty);
	}

	private PageType (String value, boolean requiresAuthentication,
			Collection<Permission> requiredPermission,
			Property[] requiredProperty) {
		this(value,
				(requiredPermission != null && !requiredPermission.isEmpty())
						|| requiresAuthentication,
				false);

		if (requiredPermission != null && !requiredPermission.isEmpty()) {
			requiredPermissions = new HashMap<String, Permission>();

			for (Permission p : requiredPermission) {
				requiredPermissions.put(p.code, p);
			}
		}

		if (requiredProperty != null && requiredProperty.length > 0) {
			requiredProperties = new HashMap<String, Property>();

			for (Property p : requiredProperty) {
				requiredProperties.put(p.name, p);
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

	public boolean requiresAdmin () {
		return requiresSystemAdmin;
	}

	private <T> Map<String, T> readonly (Map<String, T> t) {
		return t == null ? null : Collections.unmodifiableMap(t);
	}

	public Map<String, Permission> getRequiredPermissions () {
		return readonly(requiredPermissions);
	}

	public Map<String, Property> getRequiredProperties () {
		return readonly(requiredProperties);
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
