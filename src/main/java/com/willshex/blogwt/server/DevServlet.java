//
//  DevServlet.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 15 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;

import com.spacehopperstudios.utility.JsonUtils;
import com.willshex.blogwt.server.api.blog.BlogApi;
import com.willshex.blogwt.server.service.archiveentry.ArchiveEntryServiceProvider;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.post.PostServiceProvider;
import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.server.service.tag.TagServiceProvider;
import com.willshex.blogwt.shared.api.blog.call.GetPostsRequest;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.helper.RoleHelper;
import com.willshex.service.ContextAwareServlet;

/**
 * @author William Shakour (billy1380)
 *
 */
public class DevServlet extends ContextAwareServlet {

	private static final long serialVersionUID = 8911904038164388255L;
	private static final String DEV_ACCESS_CODE = "d8d20842-a8f7-11e5-bf72-cf5f3bd13298";

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.service.ContextAwareServlet#doGet() */
	@Override
	protected void doGet () throws ServletException, IOException {
		super.doGet();

		String action = REQUEST.get().getParameter("action");

		if (action != null) {
			action = action.toLowerCase();
		}

		if ("gentags".equals(action)) {
			TagServiceProvider.provide().generateTags();
		} else if ("indexall".equals(action)) {
			PostServiceProvider.provide().indexAll();
		} else if ("linkall".equals(action)) {
			PostServiceProvider.provide().linkAll();
		} else if ("clearlinks".equals(action)) {
			PostServiceProvider.provide().clearLinks();
		} else if ("archiveall".equals(action)) {
			ArchiveEntryServiceProvider.provide().generateArchive();
		} else if ("fixroles".equals(action)) {
			Collection<Role> all = RoleHelper.createAll();

			Role loaded;
			for (Role role : all) {
				loaded = RoleServiceProvider.provide().getCodeRole(role.code);

				if (loaded == null || loaded.id == null) {
					RoleServiceProvider.provide().addRole(role);
				}
			}
		} else if ("fixpermissions".equals(action)) {
			Collection<Permission> all = PermissionHelper.createAll();

			Permission loaded;
			for (Permission permission : all) {
				loaded = PermissionServiceProvider.provide().getCodePermission(
						permission.code);

				if (loaded == null || loaded.id == null) {
					PermissionServiceProvider.provide().addPermission(
							permission);
				}
			}
		} else if ("getposts".equals(action)) {
			RESPONSE.get()
					.getOutputStream()
					.print(JsonUtils.beautifyJson(new BlogApi().getPosts(
							(GetPostsRequest) new GetPostsRequest().pager(
									PagerHelper.createDefaultPager())
									.accessCode(DEV_ACCESS_CODE)).toString()));
		}
	}
}
