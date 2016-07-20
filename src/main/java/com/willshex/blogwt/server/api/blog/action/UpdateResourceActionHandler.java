//  
//  UpdateResourceActionHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 20, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.Arrays;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.ResourceValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.resource.ResourceServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.blog.call.UpdateResourceRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdateResourceResponse;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.shared.StatusType;

public final class UpdateResourceActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(UpdateResourceActionHandler.class.getName());

	public UpdateResourceResponse handle (UpdateResourceRequest input) {
		LOG.finer("Entering updateResource");
		UpdateResourceResponse output = new UpdateResourceResponse();
		try {
			ApiValidator.notNull(input, UpdateResourceRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");
			output.session = input.session = SessionValidator
					.lookupAndExtend(input.session, "input.session");

			input.session.user = UserServiceProvider.provide()
					.getUser(Long.valueOf(input.session.userKey.getId()));

			UserValidator.authorisation(input.session.user,
					Arrays.asList(PermissionServiceProvider.provide()
							.getCodePermission(
									PermissionHelper.MANAGE_RESOURCES)),
					"input.session.user");

			Resource updatedResource = input.resource;

			input.resource = ResourceValidator.lookup(input.resource,
					"input.resource");
			updatedResource = ResourceValidator.validate(updatedResource,
					"input.resource");

			input.resource.name = updatedResource.name;
			input.resource.description = updatedResource.description;

			output.resource = ResourceServiceProvider.provide()
					.updateResource(input.resource);

			UserHelper.stripPassword(
					output.session == null ? null : output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting updateResource");
		return output;
	}
}