//  
//  UpdatePageActionHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 20, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.page.action;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.PageValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.page.PageServiceProvider;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.page.call.UpdatePageRequest;
import com.willshex.blogwt.shared.api.page.call.UpdatePageResponse;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.helper.PostHelper;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.shared.StatusType;

public final class UpdatePageActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(UpdatePageActionHandler.class.getName());

	public UpdatePageResponse handle (UpdatePageRequest input) {
		LOG.finer("Entering updatePage");
		UpdatePageResponse output = new UpdatePageResponse();
		try {
			ApiValidator.notNull(input, UpdatePageRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			output.session = input.session = SessionValidator
					.lookupAndExtend(input.session, "input.session");

			List<Permission> permissions = new ArrayList<Permission>();
			Permission postPermission = PermissionServiceProvider.provide()
					.getCodePermission(PermissionHelper.MANAGE_PAGES);
			permissions.add(postPermission);

			input.session.user = UserServiceProvider.provide()
					.getUser(Long.valueOf(input.session.userKey.getId()));

			UserValidator.authorisation(input.session.user, permissions,
					"input.session.user");

			Page updatedPage = input.page;

			input.page = PageValidator.lookup(input.page, "input.page");
			updatedPage = PageValidator.validate(updatedPage, "input.page");

			input.page.hasChildren = updatedPage.hasChildren;
			input.page.parent = updatedPage.parent;
			input.page.posts = updatedPage.posts;
			input.page.priority = updatedPage.priority;
			input.page.title = updatedPage.title;
			input.page.slug = PostHelper.slugify(input.page.title);

			output.page = PageServiceProvider.provide().updatePage(input.page);

			UserHelper.stripPassword(
					output.session == null ? null : output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting updatePage");
		return output;
	}
}