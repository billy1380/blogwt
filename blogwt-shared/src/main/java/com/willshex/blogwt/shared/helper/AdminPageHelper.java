//
//  AdminPageHelper.java
//  blogwt
//
//  Copyright © 2026 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.willshex.blogwt.shared.api.datatype.Page;

/**
 * The admin menu as pages. A page with a {@link Page#route} is an admin menu
 * item, not content: it opens the client route, and the client decides who may
 * see it from its rules for that route. A page whose parent is another admin
 * page is a sub item, and an empty route is a group that only folds its
 * children.
 *
 * Setup and the <code>fixadminpages</code> dev action add whatever is missing
 * (by slug) and leave existing pages alone, so they can be edited afterwards.
 */
public class AdminPageHelper {

	public static final String PREFIX = "admin-";

	public static final String PROPERTIES = PREFIX + "properties";
	public static final String POSTS = PREFIX + "posts";
	public static final String PAGES = PREFIX + "pages";
	public static final String USERS = PREFIX + "users";
	public static final String ROLES = PREFIX + "roles";
	public static final String PERMISSIONS = PREFIX + "permissions";
	public static final String RESOURCES = PREFIX + "resources";
	public static final String COMMENTS = PREFIX + "comments";
	public static final String NOTIFICATIONS = PREFIX + "notifications";

	/**
	 * Sets of pages that a site built on blogwt adds to the admin menu, each
	 * called when the pages are created. A parent has to come before its
	 * children, and may be any page already created (or in this list) by slug.
	 */
	public static final List<java.util.function.Supplier<Collection<Page>>> extensions = new ArrayList<>();

	/**
	 * @param slug unique, and begins with {@link #PREFIX}
	 * @param title
	 * @param subtitle
	 * @param icon name of an icon
	 * @param route the client route, or empty for a group
	 * @param parentSlug slug of the parent admin page, or <code>null</code>
	 * @param priority the order among its siblings
	 */
	public static Page create (String slug, String title, String subtitle,
			String icon, String route, String parentSlug, float priority) {
		Page page = new Page().slug(slug).title(title).subtitle(subtitle)
				.icon(icon).route(route).priority(Float.valueOf(priority));

		if (parentSlug != null) {
			page.parent = new Page().slug(parentSlug);
		}

		page.hasChildren = Boolean.FALSE;
		page.commentsEnabled = Boolean.FALSE;
		page.commentsModerated = Boolean.FALSE;
		page.reactionsEnabled = Boolean.FALSE;

		return page;
	}

	public static boolean isAdminPage (Page page) {
		return page != null && page.route != null;
	}

	public static Collection<Page> createAll () {
		List<Page> all = new ArrayList<Page>();

		all.add(create(PROPERTIES, "Properties", "Change properties",
				"keyboard", "/properties", null, 1));
		all.add(create(POSTS, "Posts", "View and edit posts", "edit",
				"/posts", null, 2));
		all.add(create(PAGES, "Pages", "View and edit pages", "book",
				"/pages", null, 3));
		all.add(create(USERS, "Users", "Manage users", "face", "/users",
				null, 4));
		all.add(create(ROLES, "Roles", "View roles", "group", "/roles",
				null, 5));
		all.add(create(PERMISSIONS, "Permissions", "View permissions",
				"thumbs_up_down", "/permissions", null, 6));
		all.add(create(RESOURCES, "Resources", "Add and delete resources",
				"photo_album", "/resources", null, 7));
		all.add(create(COMMENTS, "Comments", "Review and moderate comments",
				"chat", "/comments", null, 8));
		all.add(create(NOTIFICATIONS, "Notifications",
				"View meta-notifications", "notifications",
				"/metanotifications", null, 9));

		for (java.util.function.Supplier<Collection<Page>> extension : extensions) {
			all.addAll(extension.get());
		}

		return all;
	}

}
