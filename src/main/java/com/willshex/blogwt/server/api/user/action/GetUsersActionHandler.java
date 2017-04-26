//  
//  GetUsersActionHandlerjava
//  xsdwsdl2code
//
//  Created by William Shakour on February 6, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.user.action;

import static com.willshex.blogwt.server.helper.PersistenceHelper.keyToId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.exception.AuthorisationException;
import com.willshex.blogwt.server.api.exception.DisallowedByPropertyException;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.PropertyValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.helper.UserHelper;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.relationship.RelationshipServiceProvider;
import com.willshex.blogwt.server.service.search.ISearch;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Relationship;
import com.willshex.blogwt.shared.api.datatype.RelationshipSortType;
import com.willshex.blogwt.shared.api.datatype.RelationshipTypeType;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.datatype.UserSortType;
import com.willshex.blogwt.shared.api.user.call.GetUsersRequest;
import com.willshex.blogwt.shared.api.user.call.GetUsersResponse;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.gson.web.service.server.InputValidationException;

public final class GetUsersActionHandler
		extends ActionHandler<GetUsersRequest, GetUsersResponse> {
	private static final Logger LOG = Logger
			.getLogger(GetUsersActionHandler.class.getName());

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.gson.web.service.server.ActionHandler#handle(com.willshex.
	 * gson.web.service.shared.Request,
	 * com.willshex.gson.web.service.shared.Response) */
	@Override
	protected void handle (GetUsersRequest input, GetUsersResponse output)
			throws Exception {
		ApiValidator.notNull(input, GetUsersRequest.class, "input");
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		if (input.query == null) {
			if (input.relationshipType != null) {
				output.users = getRelatedUsers(input);
			} else {
				output.users = getAllUsers(input);
			}
		} else {
			output.users = searchUsers(input);
		}

		output.pager = PagerHelper.moveForward(input.pager);
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
	 * @throws InputValidationException
	 */
	@SuppressWarnings("unchecked")
	private List<User> searchUsers (GetUsersRequest input)
			throws InputValidationException {
		ApiValidator.validateLength(input.query = input.query.trim(), 1, 255,
				"input.query");

		return ((ISearch<User>) UserServiceProvider.provide()).search(
				input.query, input.pager.start, input.pager.count,
				input.pager.sortBy, input.pager.sortDirection);
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
			// Cannot reveal users blocking you
			if (input.relationshipType == RelationshipTypeType.RelationshipTypeTypeBlock)
				ApiValidator.throwServiceError(InputValidationException.class,
						ApiError.CannotRevealRelationshipUsers,
						"input.relationshipType");

			relationships = RelationshipServiceProvider.provide()
					.getWithUserRelationships(user, input.relationshipType,
							input.pager.start, input.pager.count,
							RelationshipSortType.fromString(input.pager.sortBy),
							input.pager.sortDirection);

			if (relationships != null && !relationships.isEmpty()) {
				userIds = new ArrayList<Long>();
				for (Relationship relationship : relationships) {
					userIds.add(keyToId(relationship.oneKey));
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
					userIds.add(keyToId(relationship.anotherKey));
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

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#newOutput() */
	@Override
	protected GetUsersResponse newOutput () {
		return new GetUsersResponse();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#logger() */
	@Override
	protected Logger logger () {
		return LOG;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.api.ActionHandler#clearSensitiveFields(com.
	 * willshex.blogwt.shared.api.Response) */
	@Override
	public void clearSensitiveFields (GetUsersResponse output) {
		super.clearSensitiveFields(output);

		UserHelper.stripPassword(output.users);
	}

}
