//  
//  BlogApi.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.googlecode.objectify.Key;
import com.willshex.blogwt.server.api.exception.AuthorisationException;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.PostValidator;
import com.willshex.blogwt.server.api.validation.PropertyValidator;
import com.willshex.blogwt.server.api.validation.RoleValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.post.PostServiceProvider;
import com.willshex.blogwt.server.service.property.IPropertyService;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.blog.call.CreatePostRequest;
import com.willshex.blogwt.shared.api.blog.call.CreatePostResponse;
import com.willshex.blogwt.shared.api.blog.call.DeletePostRequest;
import com.willshex.blogwt.shared.api.blog.call.DeletePostResponse;
import com.willshex.blogwt.shared.api.blog.call.GetPostRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostResponse;
import com.willshex.blogwt.shared.api.blog.call.GetPostsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostsResponse;
import com.willshex.blogwt.shared.api.blog.call.SetupBlogRequest;
import com.willshex.blogwt.shared.api.blog.call.SetupBlogResponse;
import com.willshex.blogwt.shared.api.blog.call.UpdatePostRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdatePostResponse;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.PostSortType;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.helper.PagerHelper;
import com.willshex.blogwt.shared.api.helper.PermissionHelper;
import com.willshex.blogwt.shared.api.helper.PropertyHelper;
import com.willshex.blogwt.shared.api.helper.RoleHelper;
import com.willshex.blogwt.shared.api.helper.UserHelper;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.json.service.server.ActionHandler;
import com.willshex.gson.json.service.server.InputValidationException;
import com.willshex.gson.json.service.server.ServiceException;
import com.willshex.gson.json.service.shared.StatusType;

public final class BlogApi extends ActionHandler {
	private static final Logger LOG = Logger.getLogger(BlogApi.class.getName());

	public GetPostResponse getPost (GetPostRequest input) {
		LOG.finer("Entering getPost");
		GetPostResponse output = new GetPostResponse();
		try {
			ApiValidator.notNull(input, CreatePostRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			if (input.session != null) {
				try {
					output.session = input.session = SessionValidator
							.lookupAndExtend(input.session, "input.session");
				} catch (InputValidationException ex) {
					output.session = input.session = null;
				}
			}

			output.post = PostValidator.lookup(input.post, "input.post");

			if (output.post != null) {
				output.post.author = UserServiceProvider.provide().getUser(
						Long.valueOf(output.post.authorKey.getId()));
				UserHelper.stripPassword(output.post.author);
			}

			output.post.content = PostServiceProvider.provide().getPostContent(
					output.post);

			UserHelper.stripPassword(output.session == null ? null
					: output.session.user);

			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getPost");
		return output;
	}

	public UpdatePostResponse updatePost (UpdatePostRequest input) {
		LOG.finer("Entering updatePost");
		UpdatePostResponse output = new UpdatePostResponse();
		try {
			ApiValidator.notNull(input, UpdatePostRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");
			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");

			List<Role> roles = new ArrayList<Role>();
			roles.add(RoleHelper.createAdmin());

			List<Permission> permissions = new ArrayList<Permission>();
			Permission postPermission = PermissionServiceProvider.provide()
					.getCodePermission(PermissionHelper.MANAGE_POSTS);
			permissions.add(postPermission);

			UserValidator.authorisation(input.session.user, roles, permissions,
					"input.session.user");

			Post updatedPost = input.post;

			input.post = PostValidator.lookup(input.post, "input.post");
			input.post.content = PostServiceProvider.provide().getPostContent(
					input.post);

			updatedPost = PostValidator.validate(updatedPost, "input.post");

			input.post.commentsEnabled = updatedPost.commentsEnabled;
			input.post.content.body = updatedPost.content.body;
			input.post.listed = updatedPost.listed;
			input.post.summary = updatedPost.summary;
			input.post.tags = updatedPost.tags;
			input.post.title = updatedPost.title;

			// don't change the original publish date
			if (input.publish == Boolean.TRUE && input.post.published == null) {
				input.post.published = new Date();
			}

			PostServiceProvider.provide().updatePost(input.post);

			UserHelper.stripPassword(output.session == null ? null
					: output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting updatePost");
		return output;
	}

	public CreatePostResponse createPost (CreatePostRequest input) {
		LOG.finer("Entering createPost");
		CreatePostResponse output = new CreatePostResponse();
		try {
			ApiValidator.notNull(input, CreatePostRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");
			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");

			List<Role> roles = new ArrayList<Role>();
			roles.add(RoleHelper.createAdmin());

			List<Permission> permissions = new ArrayList<Permission>();
			Permission postPermission = PermissionServiceProvider.provide()
					.getCodePermission(PermissionHelper.MANAGE_POSTS);
			permissions.add(postPermission);

			input.session.user = UserServiceProvider.provide().getUser(
					Long.valueOf(input.session.userKey.getId()));

			UserValidator.authorisation(input.session.user, roles, permissions,
					"input.session.user");

			input.post = PostValidator.validate(input.post, "input.post");

			input.post.author = input.session.user;

			if (input.publish == Boolean.TRUE) {
				input.post.published = new Date();
			}

			input.post.listed = (input.post.listed == null ? Boolean.TRUE
					: input.post.listed);
			input.post.commentsEnabled = (input.post.commentsEnabled == null ? Boolean.FALSE
					: input.post.commentsEnabled);

			output.post = PostServiceProvider.provide().addPost(input.post);

			if (output.post != null) {
				output.post.author = UserServiceProvider.provide().getUser(
						output.post.author.id);
				UserHelper.stripPassword(output.post.author);
			}

			UserHelper.stripPassword(output.post.author);
			UserHelper.stripPassword(output.session == null ? null
					: output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting createPost");
		return output;
	}

	public DeletePostResponse deletePost (DeletePostRequest input) {
		LOG.finer("Entering deletePost");
		DeletePostResponse output = new DeletePostResponse();
		try {
			ApiValidator.notNull(input, UpdatePostRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");
			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");

			List<Role> roles = new ArrayList<Role>();
			roles.add(RoleHelper.createAdmin());

			List<Permission> permissions = new ArrayList<Permission>();
			Permission postPermission = PermissionServiceProvider.provide()
					.getCodePermission(PermissionHelper.MANAGE_POSTS);
			permissions.add(postPermission);

			UserValidator.authorisation(input.session.user, roles, permissions,
					"input.session.user");

			input.post = PostValidator.lookup(input.post, "input.post");

			PostServiceProvider.provide().deletePost(input.post);

			UserHelper.stripPassword(output.session == null ? null
					: output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting deletePost");
		return output;
	}

	public GetPostsResponse getPosts (GetPostsRequest input) {
		LOG.finer("Entering getPosts");
		GetPostsResponse output = new GetPostsResponse();
		try {
			ApiValidator.notNull(input, GetPostsRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			Boolean showAll = Boolean.FALSE;
			if (input.session != null) {
				try {
					output.session = input.session = SessionValidator
							.lookupAndExtend(input.session, "input.session");

					List<Role> roles = new ArrayList<Role>();
					roles.add(RoleHelper.createAdmin());

					List<Permission> permissions = new ArrayList<Permission>();
					Permission postPermission = PermissionServiceProvider
							.provide().getCodePermission(
									PermissionHelper.MANAGE_POSTS);
					permissions.add(postPermission);

					input.session.user = UserServiceProvider.provide().getUser(
							Long.valueOf(input.session.userKey.getId()));

					try {
						UserValidator.authorisation(input.session.user, roles,
								permissions, "input.session.user");
						showAll = Boolean.TRUE;
					} catch (AuthorisationException aEx) {

					}
				} catch (InputValidationException ex) {
					output.session = input.session = null;
				}
			}

			if (input.includePostContents == null) {
				input.includePostContents = Boolean.FALSE;
			}

			if (input.session != null && input.session.user != null) {
				output.posts = PostServiceProvider.provide()
						.getUserViewablePosts(input.session.user, showAll,
								input.includePostContents, input.pager.start,
								input.pager.count,
								PostSortType.fromString(input.pager.sortBy),
								input.pager.sortDirection);
			} else {
				output.posts = PostServiceProvider.provide().getPosts(showAll,
						input.includePostContents, input.pager.start,
						input.pager.count, PostSortType.PostSortTypePublished,
						SortDirectionType.SortDirectionTypeDescending);
			}

			Map<Key<User>, User> users = new HashMap<Key<User>, User>();

			for (Post post : output.posts) {
				if (users.get(post.authorKey) == null) {
					users.put(
							post.authorKey,
							UserHelper.stripSensitive(UserServiceProvider
									.provide()
									.getUser(
											Long.valueOf(post.authorKey.getId()))));
				}

				post.author = users.get(post.authorKey);
			}

			output.pager = PagerHelper.moveForward(input.pager);

			UserHelper.stripPassword(output.session == null ? null
					: output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getPosts");
		return output;
	}

	public SetupBlogResponse setupBlog (SetupBlogRequest input) {
		LOG.finer("Entering setupBlog");
		SetupBlogResponse output = new SetupBlogResponse();
		try {
			ApiValidator.notNull(input, SetupBlogRequest.class, "input");

			IPropertyService propertyService = PropertyServiceProvider
					.provide();
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

			RoleServiceProvider.provide().addRole(
					RoleHelper
							.createFull(RoleHelper.ADMIN,
									RoleHelper.ADMIN_NAME,
									RoleHelper.ADMIN_DESCRIPTION));
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

			UserHelper.stripPassword(output.session == null ? null
					: output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting setupBlog");
		return output;
	}
}