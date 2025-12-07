//
//  DevServlet.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 15 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.dev;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;

import com.willshex.blogwt.server.dev.actions.AdminAction;
import com.willshex.blogwt.server.dev.actions.ArchiveAllAction;
import com.willshex.blogwt.server.dev.actions.ClearLinksAction;
import com.willshex.blogwt.server.dev.actions.ClearSearchAction;
import com.willshex.blogwt.server.dev.actions.ConvertStringsAction;
import com.willshex.blogwt.server.dev.actions.DeletePostsAction;
import com.willshex.blogwt.server.dev.actions.FixMetaNotificationsAction;
import com.willshex.blogwt.server.dev.actions.FixPermissionsAction;
import com.willshex.blogwt.server.dev.actions.FixRolesAction;
import com.willshex.blogwt.server.dev.actions.GenAndShowDownloadAction;
import com.willshex.blogwt.server.dev.actions.GenTagsAction;
import com.willshex.blogwt.server.dev.actions.GetPostsAction;
import com.willshex.blogwt.server.dev.actions.IndexAction;
import com.willshex.blogwt.server.dev.actions.LinkAllAction;
import com.willshex.blogwt.server.dev.actions.ResaveAction;
import com.willshex.blogwt.server.dev.actions.StaticUrlAction;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.server.ContextAwareServlet;

/**
 * @author William Shakour (billy1380)
 *
 */
@WebServlet(name = "Dev", urlPatterns = { DevServlet.URL,
		DevServlet.URL + "/*" })
@ServletSecurity(value = @HttpConstraint(rolesAllowed = "admin"))
public class DevServlet extends ContextAwareServlet {

	public static interface AllPaged<T, E extends Enum<E>> {
		List<T> get(Integer start, Integer count, E sortBy,
				SortDirectionType sortDirection);
	}

	private static final Logger LOG = Logger
			.getLogger(DevServlet.class.getName());

	public static final String URL = "/dev";

	private Map<String, DevAction> actions;
	@Override
	protected void doPost() throws ServletException, IOException {
		doGet();
	}
	@Override
	protected void doGet() throws ServletException, IOException {
		super.doGet();

		String action = REQUEST.get().getParameter("action");

		if (action != null) {
			action = action.toLowerCase();
		}

		if (actions == null) {
			actions = new HashMap<>();
			actions.put("gentags", new GenTagsAction());
			actions.put("index", new IndexAction());
			actions.put("clearsearch", new ClearSearchAction());
			actions.put("linkall", new LinkAllAction());
			actions.put("clearlinks", new ClearLinksAction());
			actions.put("archiveall", new ArchiveAllAction());
			actions.put("fixroles", new FixRolesAction());
			actions.put("fixpermissions", new FixPermissionsAction());
			actions.put("getposts", new GetPostsAction());
			actions.put("staticurl", new StaticUrlAction());
			actions.put("fixmetanotifications",
					new FixMetaNotificationsAction());
			actions.put("admin", new AdminAction());
			actions.put("genandshowdownload", new GenAndShowDownloadAction());
			actions.put("resave", new ResaveAction());
			actions.put("deleteposts", new DeletePostsAction());
			actions.put("convertstrings", new ConvertStringsAction());
		}

		if (action != null) {
			if (actions.containsKey(action)) {
				try {
					actions.get(action).handle(REQUEST.get(), RESPONSE.get());
				} catch (Exception e) {
					LOG.log(Level.SEVERE, "Error handling action [" + action + "]",
							e);
				}
			} else {
				for (String key : actions.keySet()) {
					if (action.startsWith(key)) {
						try {
							actions.get(key).handle(REQUEST.get(),
									RESPONSE.get());
						} catch (Exception e) {
							LOG.log(Level.SEVERE,
									"Error handling action [" + action + "]", e);
						}
						break;
					}
				}
			}
		}
	}

}
