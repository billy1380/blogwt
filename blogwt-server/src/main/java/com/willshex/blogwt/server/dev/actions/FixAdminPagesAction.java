package com.willshex.blogwt.server.dev.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.willshex.blogwt.server.dev.DevAction;
import com.willshex.blogwt.server.service.page.PageServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.helper.AdminPageHelper;

/**
 * Adds the admin menu pages (see {@link AdminPageHelper}) that are missing and
 * resets the ones that exist to their definition. Safe to run again.
 */
public class FixAdminPagesAction implements DevAction {
	private static final Logger LOG = Logger
			.getLogger(FixAdminPagesAction.class.getName());

	@Override
	public void handle (HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<User> users = UserServiceProvider.provide().getUsers(
				Integer.valueOf(0), Integer.valueOf(1), null, null);

		if (users == null || users.isEmpty()) {
			LOG.warning("No users, cannot own the admin pages");
			return;
		}

		int added = addMissing(users.get(0));

		response.setContentType("text/plain");
		response.getWriter().println("Added " + added + " admin pages");
	}

	/**
	 * Adds the admin pages that are missing and puts the ones that exist back
	 * to their definition (title, icon, route, parent and priority), so a
	 * change to the definition reaches a site by running this again.
	 *
	 * @param owner the user the new pages belong to
	 * @return how many pages were added
	 */
	public static int addMissing (User owner) {
		int added = 0;

		for (Page page : AdminPageHelper.createAll()) {
			Page existing = PageServiceProvider.provide()
					.getSlugPage(page.slug, Boolean.FALSE);

			if (page.parent != null) {
				Page parent = PageServiceProvider.provide()
						.getSlugPage(page.parent.slug, Boolean.FALSE);

				if (parent == null) {
					LOG.log(Level.WARNING, "Parent [" + page.parent.slug
							+ "] of [" + page.slug + "] does not exist");
					continue;
				}

				page.parent = parent;
			}

			if (existing != null) {
				existing.title = page.title;
				existing.icon = page.icon;
				existing.route = page.route;
				existing.subtitle = null;
				existing.priority = page.priority;
				existing.parent = page.parent;

				if (existing.posts == null) {
					existing.posts = new ArrayList<>();
				}

				PageServiceProvider.provide().updatePage(existing);
				continue;
			}

			page.owner = owner;
			page.posts = new ArrayList<>();

			PageServiceProvider.provide().addPage(page);
			added++;
		}

		return added;
	}

}
