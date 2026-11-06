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
 * Setup adds the pages. The <code>fixadminpages</code> dev action adds whatever
 * is missing (by slug) and resets the others to this definition, so a change
 * here reaches an existing site by running it again.
 */
public class AdminPageHelper {

	public static final String PREFIX = "admin-";

	/**
	 * The parent of the admin menu: an admin page with an empty route, which
	 * is the Admin menu itself. It is only shown to a user who may see one of
	 * its parts.
	 */
	public static final String ADMIN = "admin";

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
	 * children, and may be any page already created (or in this list) by slug,
	 * {@link #ADMIN} for the top level. A child's priority is its parent's plus
	 * its place after the point, as in 3.1, 3.2 and, a level down, 3.11.
	 */
	public static final List<java.util.function.Supplier<Collection<Page>>> extensions = new ArrayList<>();

	/**
	 * @param slug unique, and begins with {@link #PREFIX} (or is {@link #ADMIN})
	 * @param title
	 * @param icon name of an icon
	 * @param route the client route, or empty for a group
	 * @param parentSlug slug of the parent admin page, or <code>null</code>
	 * @param priority the order among its siblings
	 */
	public static Page create (String slug, String title, String icon,
			String route, String parentSlug, float priority) {
		Page page = new Page().slug(slug).title(title).icon(icon).route(route)
				.priority(Float.valueOf(priority));

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

		all.add(create(ADMIN, "Admin", "admin", "", null, 3f));

		all.add(create(PROPERTIES, "Properties", "keyboard", "/properties",
				ADMIN, 3.1f));
		all.add(create(POSTS, "Posts", "edit", "/posts", ADMIN, 3.2f));
		all.add(create(PAGES, "Pages", "book", "/pages", ADMIN, 3.3f));
		all.add(create(USERS, "Users", "face", "/users", ADMIN, 3.4f));
		all.add(create(ROLES, "Roles", "group", "/roles", ADMIN, 3.5f));
		all.add(create(PERMISSIONS, "Permissions", "thumbs_up_down",
				"/permissions", ADMIN, 3.6f));
		all.add(create(RESOURCES, "Resources", "photo_album", "/resources",
				ADMIN, 3.7f));
		all.add(create(COMMENTS, "Comments", "chat", "/comments", ADMIN,
				3.8f));
		all.add(create(NOTIFICATIONS, "Notifications", "notifications",
				"/metanotifications", ADMIN, 3.9f));

		for (java.util.function.Supplier<Collection<Page>> extension : extensions) {
			all.addAll(extension.get());
		}

		return all;
	}

}
