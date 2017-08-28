//
//  DevServlet.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 15 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.willshex.blogwt.server.api.blog.action.GetPostsActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.helper.SearchHelper;
import com.willshex.blogwt.server.service.archiveentry.ArchiveEntryServiceProvider;
import com.willshex.blogwt.server.service.metanotification.MetaNotificationServiceProvider;
import com.willshex.blogwt.server.service.page.PageServiceProvider;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider;
import com.willshex.blogwt.server.service.post.PostServiceProvider;
import com.willshex.blogwt.server.service.resource.ResourceServiceProvider;
import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.server.service.search.ISearch;
import com.willshex.blogwt.server.service.tag.TagServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.blog.call.GetPostsRequest;
import com.willshex.blogwt.shared.api.datatype.MetaNotification;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.helper.MetaNotificationHelper;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.helper.RoleHelper;
import com.willshex.server.ContextAwareServlet;
import com.willshex.service.ServiceDiscovery;
import com.willshex.utility.JsonUtils;

/**
 * @author William Shakour (billy1380)
 *
 */
@WebServlet(name = "Dev", urlPatterns = { "/dev", "/dev/*" })
@ServletSecurity(value = @HttpConstraint(rolesAllowed = "admin"))
public class DevServlet extends ContextAwareServlet {

	private static final long serialVersionUID = 8911904038164388255L;

	private static final Logger LOG = Logger
			.getLogger(DevServlet.class.getName());

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
		} else if (action != null && action.startsWith("index")) {
			PageServiceProvider.provide();
			PostServiceProvider.provide();
			UserServiceProvider.provide();

			((ISearch<?>) ServiceDiscovery
					.getService("blogwt." + action.replace("index", "")))
							.indexAll();
		} else if ("clearsearch".equals(action)) {
			PersistenceServiceProvider.provide();

			String name = REQUEST.get().getParameter("index");
			String ids = REQUEST.get().getParameter("ids");
			String[] split = ids.split(",");

			for (String id : split) {
				SearchHelper.deleteSearch(name, id);
			}
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
				loaded = PermissionServiceProvider.provide()
						.getCodePermission(permission.code);

				if (loaded == null || loaded.id == null) {
					PermissionServiceProvider.provide()
							.addPermission(permission);
				}
			}
		} else if ("getposts".equals(action)) {
			RESPONSE.get().getOutputStream()
					.print(JsonUtils.beautifyJson((new GetPostsActionHandler())
							.handle((GetPostsRequest) new GetPostsRequest()
									.showAll(Boolean.TRUE)
									.pager(PagerHelper.createDefaultPager())
									.accessCode(ApiValidator.DEV_ACCESS_CODE))
							.toString()));
		} else if ("staticurl".equals(action)) {
			List<Resource> resources = ResourceServiceProvider.provide()
					.getResources(Integer.valueOf(0),
							Integer.valueOf(Integer.MAX_VALUE), null, null);
			for (Resource resource : resources) {
				if (resource.description != null
						&& !resource.description.contains("Static url")) {

					try {
						resource.description += "\nStatic url:\n"
								+ ImagesServiceFactory.getImagesService()
										.getServingUrl(ServingUrlOptions.Builder
												.withBlobKey(new BlobKey(
														resource.data.replace(
																"gs://", ""))));
						ResourceServiceProvider.provide()
								.updateResource(resource);
					} catch (Throwable e) {
						if (LOG.isLoggable(Level.FINE)) {
							LOG.fine("Could not update resource");
						}
					}
				}
			}
		} else if ("fixmetanotifications".equals(action)) {
			List<MetaNotification> metas = MetaNotificationHelper.createAll();

			for (MetaNotification meta : metas) {
				if (MetaNotificationServiceProvider.provide()
						.getCodeMetaNotification(meta.code) == null) {
					meta = MetaNotificationServiceProvider.provide()
							.addMetaNotification(meta);
					LOG.info("added meta notification [" + meta.code
							+ "] with id [" + meta.id + "]");
				} else {
					LOG.info("Meta notification [" + meta.code
							+ "] already exists");
				}
			}
		} else if ("admin".equals(action)) {
			User user = UserServiceProvider.provide()
					.getUsernameUser(REQUEST.get().getParameter("user"));
			UserServiceProvider.provide().addUserRolesAndPermissions(user,
					Arrays.asList(RoleServiceProvider.provide()
							.getCodeRole(RoleHelper.ADMIN)),
					null);
		}
	}
}
