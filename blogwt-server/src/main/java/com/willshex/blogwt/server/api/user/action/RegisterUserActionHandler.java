//  
//  RegisterUserActionHandlerjava
//  xsdwsdl2code
//
//  Created by William Shakour on February 6, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.user.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.PermissionValidator;
import com.willshex.blogwt.server.api.validation.PropertyValidator;
import com.willshex.blogwt.server.api.validation.RoleValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.helper.UserHelper;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.user.call.RegisterUserRequest;
import com.willshex.blogwt.shared.api.user.call.RegisterUserResponse;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.blogwt.shared.helper.TagHelper;
import com.willshex.gson.web.service.server.InputValidationException;

public final class RegisterUserActionHandler
		extends ActionHandler<RegisterUserRequest, RegisterUserResponse> {
	private static final Logger LOG = Logger
			.getLogger(RegisterUserActionHandler.class.getName());
	@Override
	protected void handle (RegisterUserRequest input,
			RegisterUserResponse output) throws Exception {
		ApiValidator.request(input, RegisterUserRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		if (input.session != null) {
			try {
				output.session = input.session = SessionValidator
						.lookupCheckAndExtend(input.session, "input.session");

				UserValidator.authorisation(input.session.user,
						Arrays.asList(PermissionServiceProvider.provide()
								.getCodePermission(
										PermissionHelper.MANAGE_USERS)),
						"input.session.user");
			} catch (InputValidationException ex) {
				output.session = input.session = null;
			}
		} else {
			PropertyValidator
					.ensureTrue(PropertyHelper.ALLOW_USER_REGISTRATION);
		}

		input.user = UserValidator.validate(input.user, "input.user");

		List<String> codes;

		List<Permission> permissions = null;
		Property property = PropertyServiceProvider.provide()
				.getNamedProperty(PropertyHelper.NEW_USER_PERMISSIONS);

		if (!PropertyHelper.isEmpty(property)
				&& !PropertyHelper.NONE_VALUE.equals(property.value)) {
			codes = TagHelper.convertToTagList(property.value, true);

			for (String code : codes) {
				if (permissions == null) {
					permissions = new ArrayList<Permission>();
				}

				permissions.add(new Permission().code(code.toUpperCase()));
			}

			permissions = PermissionValidator.lookupAll(permissions,
					PropertyHelper.NEW_USER_PERMISSIONS);
		}

		List<Role> roles = null;
		property = PropertyServiceProvider.provide()
				.getNamedProperty(PropertyHelper.NEW_USER_ROLES);

		if (!PropertyHelper.isEmpty(property)
				&& !PropertyHelper.NONE_VALUE.equals(property.value)) {
			codes = TagHelper.convertToTagList(property.value, true);

			for (String code : codes) {
				if (roles == null) {
					roles = new ArrayList<Role>();
				}

				roles.add(new Role().code(code.toUpperCase()));
			}

			roles = RoleValidator.lookupAll(roles,
					PropertyHelper.NEW_USER_ROLES);
		}

		input.user.permissions = permissions;
		input.user.roles = roles;

		output.user = UserServiceProvider.provide().addUser(input.user);
		UserServiceProvider.provide().verifyAccount(output.user);
	}
	@Override
	protected RegisterUserResponse newOutput () {
		return new RegisterUserResponse();
	}
	@Override
	protected Logger logger () {
		return LOG;
	}
	@Override
	public void clearSensitiveFields (RegisterUserResponse output) {
		super.clearSensitiveFields(output);

		UserHelper.stripPassword(output.user);
	}
}