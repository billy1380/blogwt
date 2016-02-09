//  
//  GetUsersActionHandlerjava
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

import com.willshex.blogwt.server.api.exception.AuthorisationException;
import com.willshex.blogwt.server.api.exception.DisallowedByPropertyException;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.PropertyValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.helper.UserHelper;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.relationship.RelationshipServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Relationship;
import com.willshex.blogwt.shared.api.datatype.RelationshipSortType;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.datatype.UserSortType;
import com.willshex.blogwt.shared.api.user.call.GetUsersRequest;
import com.willshex.blogwt.shared.api.user.call.GetUsersResponse;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.server.InputValidationException;
import com.willshex.gson.web.service.shared.StatusType;

public final class GetUsersActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(GetUsersActionHandler.class.getName());

	public GetUsersResponse handle (GetUsersRequest input) {
		LOG.finer("Entering getUsers");
		GetUsersResponse output = new GetUsersResponse();
		try {
			ApiValidator.notNull(input, GetUsersRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			output.session = input.session = SessionValidator
					.lookupAndExtend(input.session, "input.session");

			output.session.user = UserServiceProvider.provide()
					.getUser(Long.valueOf(output.session.userKey.getId()));

			if (input.relationshipType != null) {
				output.users = getRelatedUsers(input);
			} else {
				output.users = getAllUsers(input);
			}

			UserHelper.stripPassword(output.users);

			output.pager = PagerHelper.moveForward(input.pager);

			UserHelper.stripPassword(
					output.session == null ? null : output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getUsers");
		return output;
	}

	/**
	 * @param input
	 * @return
	 * @throws AuthorisationException 
	 */
	private List<User> getAllUsers (GetUsersRequest input)
			throws AuthorisationException {
		UserValidator.authorisation(input.session.user,
				Arrays.asList(PermissionServiceProvider.provide()
						.getCodePermission(PermissionHelper.MANAGE_USERS)),
				"input.session.user");

		return UserServiceProvider.provide().getUsers(input.pager.start,
				input.pager.count, UserSortType.fromString(input.pager.sortBy),
				input.pager.sortDirection);

	}

	/**
	 * @param input
	 * @return
	 * @throws DisallowedByPropertyException 
	 * @throws InputValidationException 
	 */
	private List<User> getRelatedUsers (GetUsersRequest input)
			throws DisallowedByPropertyException, InputValidationException {
		List<User> relatedUsers = null;
		User user = null;

		PropertyValidator.ensureTrue(PropertyHelper.ALLOW_USER_REGISTRATION,
				PropertyHelper.ENABLE_USER_RELATIONSHIPS);

		if (input.user == null) {
			user = input.user = UserValidator.validate(input.user,
					"input.user");
		} else {
			user = input.session.user;
		}

		List<Relationship> relationships = null;
		List<Long> userIds = null;

		if (Boolean.TRUE.equals(input.userIsOther)) {
			relationships = RelationshipServiceProvider.provide()
					.getWithUserRelationships(user, input.relationshipType,
							input.pager.start, input.pager.count,
							RelationshipSortType.fromString(input.pager.sortBy),
							input.pager.sortDirection);

			if (relationships != null && !relationships.isEmpty()) {
				userIds = new ArrayList<Long>();
				for (Relationship relationship : relationships) {
					userIds.add(Long.valueOf(relationship.oneKey.getId()));
				}
			}
		} else {
			relationships = RelationshipServiceProvider.provide()
					.getUserRelationships(user, input.relationshipType,
							input.pager.start, input.pager.count,
							RelationshipSortType.fromString(input.pager.sortBy),
							input.pager.sortDirection);

			if (relationships != null && !relationships.isEmpty()) {
				userIds = new ArrayList<Long>();
				for (Relationship relationship : relationships) {
					userIds.add(Long.valueOf(relationship.anotherKey.getId()));
				}
			}
		}

		if (userIds != null) {
			// order might become important later for now, it is not
			relatedUsers = UserServiceProvider.provide()
					.getIdUserBatch(userIds);
		}

		return relatedUsers;
	}

}