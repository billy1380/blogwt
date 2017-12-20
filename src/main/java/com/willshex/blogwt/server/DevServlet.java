//
//  DevServlet.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 15 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server;

import java.io.IOException;
import java.util.ArrayList;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.willshex.blogwt.server.api.blog.action.GetPostsActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.helper.SearchHelper;
import com.willshex.blogwt.server.service.archiveentry.ArchiveEntryServiceProvider;
import com.willshex.blogwt.server.service.generateddownload.GeneratedDownloadServiceProvider;
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
import com.willshex.blogwt.shared.api.datatype.GeneratedDownload;
import com.willshex.blogwt.shared.api.datatype.MetaNotification;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.helper.MetaNotificationHelper;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.helper.bidly.RoleHelper;
import com.willshex.blogwt.shared.page.Stack;
import com.willshex.blogwt.shared.page.search.Filter;
import com.willshex.server.ContextAwareServlet;
import com.willshex.service.ServiceDiscovery;
import com.willshex.utility.JsonUtils;

/**
 * @author William Shakour (billy1380)
 *
 */
@SuppressWarnings("serial")
@WebServlet(name = "Dev", urlPatterns = { DevServlet.URL,
		DevServlet.URL + "/*" })
@ServletSecurity(value = @HttpConstraint(rolesAllowed = "admin"))
public class DevServlet extends ContextAwareServlet {

	private static final Logger LOG = Logger
			.getLogger(DevServlet.class.getName());

	public static final String URL = "/dev";

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.server.ContextAwareServlet#doPost() */
	@Override
	protected void doPost () throws ServletException, IOException {
		doGet();
	}

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
			all.stream().forEach(role -> {
				Role loaded = RoleServiceProvider.provide()
						.getCodeRole(role.code);

				if (loaded == null || loaded.id == null) {
					RoleServiceProvider.provide().addRole(role);
				}

				if (role.permissions != null) {
					role.permissions.stream().forEach(i -> {
						Permission lp = PermissionServiceProvider.provide()
								.getCodePermission(i.code);

						if (lp == null) {
							if (LOG.isLoggable(Level.WARNING)) {
								LOG.warning(
										"Could not find permission with code ["
												+ i.code
												+ "], might want to run [fixpermissions] action");
							}
						} else {
							if (loaded.permissions == null) {
								loaded.permissions = new ArrayList<>();
							}

							loaded.permissions.add(lp);
						}
					});
					RoleServiceProvider.provide().updateRole(loaded);
				}
			});
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
			JsonObject object;
			for (Resource resource : resources) {
				if (resource.properties != null) {
					if (resource.properties.contains(":image")) {
						resource.properties = resource.properties
								.replace(":image", ":\"image")
								.replace("}", "\"}");
					}

					object = new JsonParser().parse(resource.properties)
							.getAsJsonObject();
				} else {
					object = new JsonObject();
				}

				if (!object.has("staticUrl") || object.get("staticUrl")
						.getAsString().startsWith("http")) {
					try {
						object.addProperty("staticUrl", ImagesServiceFactory
								.getImagesService()
								.getServingUrl(ServingUrlOptions.Builder
										.withBlobKey(new BlobKey(resource.data
												.replace("gs://", ""))))
								.replaceFirst("https:\\/\\/", "//")
								.replaceFirst("http:\\/\\/", "//"));
					} catch (Throwable e) {
						if (LOG.isLoggable(Level.FINE)) {
							LOG.fine("Could not update resource");
						}
					}

					resource.properties = object.toString();

					ResourceServiceProvider.provide().updateResource(resource);
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
		} else if ("genandshowdownload".equals(action)) {
			String idParam = REQUEST.get().getParameter("id");
			Long id = Long.valueOf(idParam);
			GeneratedDownload d = GeneratedDownloadServiceProvider.provide()
					.getGeneratedDownload(id);
			Stack stack = Stack.parse(d.parameters);
			Filter filter = Filter.fromStack(stack);
			switch (filter.type) {
			default:
				break;
			}
		}
	}
}
