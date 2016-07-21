//  
//  SetupBlogActionHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 20, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.PropertyValidator;
import com.willshex.blogwt.server.api.validation.RoleValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.property.IPropertyService;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.blog.call.SetupBlogRequest;
import com.willshex.blogwt.shared.api.blog.call.SetupBlogResponse;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.blogwt.shared.helper.RoleHelper;
import com.willshex.gson.web.service.server.ServiceException;

public final class SetupBlogActionHandler
		extends ActionHandler<SetupBlogRequest, SetupBlogResponse> {
	private static final Logger LOG = Logger
			.getLogger(SetupBlogActionHandler.class.getName());

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.gson.web.service.server.ActionHandler#handle(com.willshex.
	 * gson.web.service.shared.Request,
	 * com.willshex.gson.web.service.shared.Response) */
	@Override
	protected void handle (SetupBlogRequest input, SetupBlogResponse output)
			throws Exception {
		ApiValidator.notNull(input, SetupBlogRequest.class, "input");

		IPropertyService propertyService = PropertyServiceProvider.provide();
		if (propertyService.getNamedProperty(PropertyHelper.TITLE) != null)
			ApiValidator.throwServiceError(ServiceException.class,
					ApiError.ExistingSetup, "input.properties");

		ApiValidator.request(input, SetupBlogRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");
		ApiValidator.notNull(input.properties, Property.class,
				"input.properties");
		ApiValidator.notNull(input.users, User.class, "input.users");

		input.properties = PropertyValidator.setup(input.properties,
				"input.properties");

		propertyService.addPropertyBatch(input.properties);

		RoleServiceProvider.provide()
				.addRole(RoleHelper.createFull(RoleHelper.ADMIN,
						RoleHelper.ADMIN_NAME, RoleHelper.ADMIN_DESCRIPTION));
		for (Permission permission : PermissionHelper.createAll()) {
			PermissionServiceProvider.provide().addPermission(permission);
		}

		input.users = UserValidator.validateAll(input.users, "input.users");

		for (User user : input.users) {
			// users are either admins or nothing
			user.roles = RoleValidator.lookupAll(user.roles,
					"input.users[n].roles");

			// users added at startup are verified
			user.verified = Boolean.TRUE;
		}

		UserServiceProvider.provide().addUserBatch(input.users);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#newOutput() */
	@Override
	protected SetupBlogResponse newOutput () {
		return new SetupBlogResponse();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#logger() */
	@Override
	protected Logger logger () {
		return LOG;
	}
}