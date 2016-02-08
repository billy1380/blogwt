//  
//  FollowUsersActionHandlerjava
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

import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.PropertyValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.relationship.RelationshipServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Relationship;
import com.willshex.blogwt.shared.api.datatype.RelationshipTypeType;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.user.call.FollowUsersRequest;
import com.willshex.blogwt.shared.api.user.call.FollowUsersResponse;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.server.InputValidationException;
import com.willshex.gson.web.service.shared.StatusType;

public final class FollowUsersActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(FollowUsersActionHandler.class.getName());

	public FollowUsersResponse handle (FollowUsersRequest input) {
		LOG.finer("Entering followUsers");
		FollowUsersResponse output = new FollowUsersResponse();
		try {
			PropertyValidator.ensureTrue(PropertyHelper.ALLOW_USER_REGISTRATION,
					PropertyHelper.ENABLE_USER_RELATIONSHIPS);

			ApiValidator.notNull(input, FollowUsersRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");
			output.session = input.session = SessionValidator
					.lookupAndExtend(input.session, "input.session");

			input.session.user = UserServiceProvider.provide()
					.getUser(Long.valueOf(input.session.userKey.getId()));

			User user = null;
			if (input.user == null) {
				user = input.session.user;
			} else {
				user = input.user;
				UserValidator.validate(user, "input.user");

				if (!user.id.equals(input.session.user.id)) {
					UserValidator.authorisation(input.session.user,
							Arrays.asList(PermissionServiceProvider.provide()
									.getCodePermission(
											PermissionHelper.MANAGE_USERS)),
							"input.session.user");
				}
			}

			ApiValidator.notNull(input.others, User.class, "input.others");

			List<Relationship> added = null;

			if (!Boolean.TRUE.equals(input.un)) {
				added = new ArrayList<Relationship>();
			}

			for (User other : input.others) {
				try {
					// validate each user
					other = UserValidator.validate(other, "input.other[n]");

					// add a relationship for the validated users (skip failures)	
					if (Boolean.TRUE.equals(input.un)) {
						// un-follow
						RelationshipServiceProvider.provide()
								.deleteUsersRelationship(user, other,
										RelationshipTypeType.RelationshipTypeTypeFollow);
					} else {
						// follow
						added.add(RelationshipServiceProvider.provide()
								.addUsersRelationship(user, other,
										RelationshipTypeType.RelationshipTypeTypeFollow));
					}
				} catch (InputValidationException inEx) {
					// just skip that user if the user is not valid
				}
			}

			UserHelper.stripPassword(
					output.session == null ? null : output.session.user);

			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting followUsers");
		return output;
	}
}